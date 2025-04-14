package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import gov.epa.ccte.api.rapidtox.session.repository.SessionRepository;
import gov.epa.ccte.api.rapidtox.sessionreport.service.SessionReportGenerator;
import gov.epa.ccte.api.rapidtox.session.model.Session;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import gov.epa.ccte.api.rapidtox.sessionreport.model.SessionReport;
import gov.epa.ccte.api.rapidtox.sessionreport.service.SessionReportService;
import gov.epa.ccte.api.rapidtox.util.JwtHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import gov.epa.ccte.api.rapidtox.sessionreport.repository.SessionReportRepository;

@Slf4j
@RestController
@RequestMapping(value = "report")
@RequiredArgsConstructor
public class SessionReportResource {

	private final SessionReportGenerator reportGenerator;
	
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SessionReportRepository generatedReportRepository;

    private final SessionRepository sessionRepository;

    private  final SessionReportService rapidtoxService;

    // note: this is for debugging purposes to capture the raw JSON blob in the request, unparsed/tokenized/etc
    @PostMapping(value = "/single-chemical1")
    public String generateReport(@RequestBody String reportRequestJson, JwtAuthenticationToken principal) {
        log.info("request {}", reportRequestJson);
        ObjectMapper om = new ObjectMapper();
        om.addHandler(new DeserializationProblemHandler() {
            @Override
            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                log.warn("unknown property {} trying to fill in {}", propertyName, beanOrClass.getClass().getName());
                return true;
            }
        });
        try {
            ReportRequest request = om.readValue(reportRequestJson, ReportRequest.class);
            return generateReport(request, principal);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @PostMapping(value = "/single-chemical")
    public String generateReport(@RequestBody ReportRequest reportRequest, JwtAuthenticationToken principal){
        try {
            //Create the headers and response entity to return the single chemical pdf file
            reportRequest.setUsername(JwtHelper.emailFor(principal));
            byte[] bytes = reportGenerator.generateSingleReport(reportRequest);
            return rapidtoxService.postReportToClowder(reportRequest.getDtxsid(), "SINGLE",bytes,reportRequest.getReportDesc(),reportRequest.getSessionKey(),reportRequest.getUsername());
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the single chemical pdf report.", e);
            return null;
        }
    }

    @PostMapping(value = "/single-chemical/pdf")
    public ResponseEntity<byte[]> generateReportOnly(@RequestBody ReportRequest reportRequest) {
        try {
            byte[] bytes = reportGenerator.generateSingleReport(reportRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "generated.pdf");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("some error has occurred while preparing the single chemical pdf report: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/multi-chemicals")
    public String batchReportChemical(@RequestBody ReportRequest[] reportRequests){
        try {
            log.debug("request has {} number of chemicals", reportRequests.length);
            byte[] report = reportGenerator.generateBatchReport(reportRequests);
            return rapidtoxService.postReportToClowder(reportRequests[0].getDtxsid(),"BATCH", report, reportRequests[0].getReportDesc(), reportRequests[0].getSessionKey(), reportRequests[0].getUsername());
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the multi chemical pdf report.", e);
            return null;
        }
    }

    @GetMapping("/generated-reports/{sessionKey}")
    public List<SessionReport> generatedReports(@PathVariable("sessionKey") String sessionKey){

        try {

            log.debug("retrieving generated reports for session key {}", sessionKey);

            Session session = sessionRepository.findBySessionKey(sessionKey.trim());

            List<String> sessionKeys = new ArrayList<>();

            sessionKeys.add(session.getSessionKey().trim());

            if (session.getOriginalSessionKey() != null) {
                sessionKeys.add(session.getOriginalSessionKey().trim());
            }

            return this.generatedReportRepository.findAllBySessionKeyInOrderByCreatedAtDesc(sessionKeys);
        } catch (final Exception e) {
            log.error("Some error has occurred while gathering previous session reports.", e);
            return null;
        }
    }
}
