package gov.epa.ccte.api.rapidtox.landscapereport.controller;

import gov.epa.ccte.api.rapidtox.landscapereport.model.LandscapeReport;
import gov.epa.ccte.api.rapidtox.chemical.repository.ChemicalSearchRepository;
import gov.epa.ccte.api.rapidtox.landscapereport.repository.LandscapeReportRepository;
import gov.epa.ccte.api.rapidtox.landscapereport.service.LandscapeService;
import gov.epa.ccte.api.rapidtox.landscapereport.service.LandscapeReportGenerator;
import gov.epa.ccte.api.rapidtox.session.service.SessionService;
import static gov.epa.ccte.api.rapidtox.util.JwtHelper.emailFor;
import gov.epa.ccte.api.rapidtox.web.rest.errors.NoDtxsidFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "landscape")
@RequiredArgsConstructor
public class LandscapeReportResource {

    private final LandscapeReportRepository landscapeReportRepository;

    private final ChemicalSearchRepository chemicalSearchRepository;

    private final LandscapeService landscapeService;

    private final LandscapeReportGenerator reportGenerator;

    private final ApplicationContext applicationContext;
    
    private final SessionService sessionService;
    
    private final Random rng;

    @PostMapping("/landscape-reports-check")
    public List<LandscapeReportCheckResponse> landscapeReportsCheck(@RequestBody LandscapeReportRequest landscapeReportRequest) {

        log.debug("dtxsids list={}", landscapeReportRequest.getDtxsids());
        log.debug("workflow {}", landscapeReportRequest.getWorkflow());

        List<LandscapeReportCheckResponse> landscapeReportCheckList = new ArrayList<>();
        final String workflow = landscapeReportRequest.getWorkflow();
        for (String dtxsid : landscapeReportRequest.getDtxsids()) {
            if (!landscapeReportRepository.existsByDtxsidAndWorkflow(dtxsid, workflow)) {

                LandscapeReportCheckResponse landscapeReportCheck = buildReportCheck(dtxsid, workflow, chemicalSearchRepository.findFirstByDtxsid(dtxsid).getPreferredName());
                landscapeReportCheckList.add(landscapeReportCheck);

            } else if (landscapeReportRepository.existsByDtxsidAndWorkflowAndGenerate(dtxsid, workflow, true)) {
                List<LandscapeReport> landscapeReportList = landscapeReportRepository.findByDtxsidAndWorkflow(dtxsid, workflow);

                for (LandscapeReport report : landscapeReportList) {
                    report.setGenerate(false);
                    landscapeReportRepository.save(report);
                }

                LandscapeReportCheckResponse landscapeReportCheck = buildReportCheck(dtxsid, workflow, chemicalSearchRepository.findFirstByDtxsid(dtxsid).getPreferredName());
                landscapeReportCheckList.add(landscapeReportCheck);
            }

        }

        return landscapeReportCheckList;
    }

    private LandscapeReportCheckResponse buildReportCheck(String dtxsid, final String workflow, final String xxx) {
        LandscapeReportCheckResponse landscapeReportCheck = new LandscapeReportCheckResponse();
        landscapeReportCheck.setDtxsid(dtxsid);
        landscapeReportCheck.setWorkflow(workflow);
        landscapeReportCheck.setPreferredName(xxx);
        return landscapeReportCheck;
    }

    @PostMapping("/landscape-reports")
    public List<LandscapeReport> getLandscapeReports(@RequestBody LandscapeReportRequest landscapeReportRequest, JwtAuthenticationToken principal) {

        final List<String> dtxsids = landscapeReportRequest.getDtxsids();
        final String workflow = landscapeReportRequest.getWorkflow();
        final String sessionKey = landscapeReportRequest.getSessionKey();
        
        sessionService.validateSessionOwnedByPrincipal(sessionKey, principal);
        
        log.debug("dtxsid list={}", dtxsids);
        log.debug("workflow {}", workflow);
        
        final LandscapeReport landscapeReport = new LandscapeReport();

        for (String dtxsid : dtxsids) {
            if (!landscapeReportRepository.existsByDtxsidAndWorkflow(dtxsid, workflow)) {
                landscapeReport.setSessionKey(sessionKey);
                landscapeReport.setWorkflow(workflow);
                landscapeReport.setDtxsid(dtxsid);
                landscapeReport.setGenerated(false);
                landscapeReport.setReportId(rng.nextInt()); // bk: any idea why are we doing this instead of letting JPA assign it at creation?
                landscapeReport.setGenerate(true);
                landscapeReport.setVersion(1);
                landscapeReportRepository.save(landscapeReport);
            } else {
                LandscapeReport landscapeReportTemp = landscapeReportRepository.findFirstByDtxsidAndWorkflowOrderByVersionDesc(dtxsid, workflow);
                int currentVersion = landscapeReportTemp.getVersion();
                int newVersion = currentVersion + 1;
                landscapeReportTemp.setVersion(newVersion);
                landscapeReportRepository.save(landscapeReportTemp);
            }

        }

        List<LandscapeReport> landscapeReports = landscapeReportRepository.findBySessionKey(sessionKey);
        log.debug("{} records found for the given session key", landscapeReports.size());

        return landscapeReports;
    }

    @GetMapping("/landscape-reports/clowder-id")
    public String getLandscapeReportUrl(
            @RequestParam(name = "dtxsid", required = true) String dtxsid,
            @RequestParam(name = "sessionKey", required = true) String sessionKey,
            @RequestParam(name = "workflow", required = true) String workflow) {

        log.debug("dtxsid {}", dtxsid);
        log.debug("sessionKey {}", sessionKey);
        log.debug("workflow {}", workflow);

        LandscapeReport landscapeReport = landscapeReportRepository.findFirstByDtxsidAndWorkflowOrderByVersionDesc(dtxsid, workflow);
        if (landscapeReport == null) {
            throw new NoDtxsidFoundException(dtxsid, sessionKey);
        }

        String clowderId = null;
        if (landscapeReport.isGenerated()) {
            clowderId = landscapeReport.getClowderId();
        }
        return clowderId;

    }

    @PostMapping(value = "/generate-landscape-report")
    public String generateReport(@RequestBody GenerateLandscapeReportRequest landscapeGenerateChemical, JwtAuthenticationToken principal) {
        try {
            // does logged-in user own the session?
            sessionService.validateSessionOwnedByPrincipal(landscapeGenerateChemical.getSessionKey(), principal);
            
            // ignore/override username in request; use the logged-in username
            landscapeGenerateChemical.setUsername(emailFor(principal));
            String clowderId = landscapeService.generateReportAndUploadToClowder(landscapeGenerateChemical);
            return clowderId;
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the landscape pdf report.", e);
            return null;
        }
    }

}
