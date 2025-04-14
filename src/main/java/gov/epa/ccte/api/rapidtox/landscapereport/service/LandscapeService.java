package gov.epa.ccte.api.rapidtox.landscapereport.service;

import gov.epa.ccte.api.rapidtox.hazard.service.HazardComparator;
import gov.epa.ccte.api.rapidtox.bioactivity.service.BioactivityService;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportHazardData;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportPhyschem;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportPhyschemData;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportOdorThreshold;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivityModel;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportOdorThresholdData;
import gov.epa.ccte.api.rapidtox.sessionreport.HazardToHazardDtoMapper;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivitySummary;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivityBERSummary;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivityData;
import static gov.epa.ccte.api.rapidtox.config.RapidToxConstants.ENVFATE_PROPERTY_NAMES;
import static gov.epa.ccte.api.rapidtox.config.RapidToxConstants.PHYSCHEM_PROPERTY_NAMES;
import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivityBerSummary;
import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivitySummary;
import gov.epa.ccte.api.rapidtox.chemical.model.OdorThreshold;
import gov.epa.ccte.api.rapidtox.landscapereport.model.LandscapeReport;
import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemSummary;
import gov.epa.ccte.api.rapidtox.landscapereport.LandscapeReportData;
import gov.epa.ccte.api.rapidtox.bioactivity.repository.ToxcastPodRepository;
import gov.epa.ccte.api.rapidtox.chemical.repository.OdorThresholdRepository;
import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import gov.epa.ccte.api.rapidtox.landscapereport.repository.LandscapeReportRepository;
import gov.epa.ccte.api.rapidtox.physchem.repository.PhyschemSummaryRepository;
import gov.epa.ccte.api.rapidtox.landscapereport.controller.GenerateLandscapeReportRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class LandscapeService {

    private final LandscapeReportRepository landscapeReportRepository;
    private final HazardRepository hazardRepo;
    private final PhyschemSummaryRepository physchemRepo;
    private final OdorThresholdRepository odorRepo;
    private final BioactivityService bioService;
    private final ToxcastPodRepository bioactivityModelRepo;
    private final LandscapeReportGenerator reportGenerator;
    private final HazardToHazardDtoMapper hazardMapper;

    @Value("${clowder.base-url}")
    private String clowderBaseUrl;

    @Value("${clowder.write-key}")
    private String apiKey;

    @Value("${clowder.landscape}")
    private String dataset;

    public String exportReportToClowder(String dtxsid, String workflow, byte[] pdfBytes, String sessionKey, String username) throws IOException {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httppost = new HttpPost(generateUploadUrl());

            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = dateObj.format(formatter);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            final String filename = filenameFor(dtxsid, sessionKey, date);
            builder.addBinaryBody("File", pdfBytes, CONTENT_TYPE_APP_PDF, filename);
            HttpEntity entity = builder.build();
            httppost.setEntity(entity);
            response = httpclient.execute(httppost);
        } catch (Exception ex) {
            log.error("Exception during the http post request in LandscapeService. Error message: " + ex.getMessage());
        }

        String clowderId = null;

        if (response != null && response.getEntity() != null && dtxsid != null) {

            try {

                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject json = new JSONObject(responseBody);

                if (!landscapeReportRepository.existsByDtxsidAndWorkflow(dtxsid, workflow)) {
                    clowderId = json.get("id").toString();
                    LandscapeReport landscapeReport = landscapeReportFor(dtxsid, sessionKey, clowderId, username, 1, workflow);
                    landscapeReportRepository.save(landscapeReport);
                } else {
                    LandscapeReport landscapeReportTemp = landscapeReportRepository.findFirstByDtxsidAndWorkflowOrderByVersionDesc(dtxsid, workflow);
                    int newVersion = (landscapeReportTemp.getVersion() + 1);

                    clowderId = json.get("id").toString();
                    LandscapeReport landscapeReport = landscapeReportFor(dtxsid, sessionKey, clowderId, username, newVersion, workflow);
                    landscapeReportRepository.save(landscapeReport);
                }

            } catch (Exception ex) {
                log.error("Exception during the parsing of the response in LandscapeService. Error message: " + ex.getMessage());
                response.close();
                httpclient.close();
            }
        }

        response.close();
        httpclient.close();

        return clowderId;
    }
    private static final ContentType CONTENT_TYPE_APP_PDF = ContentType.create("application/pdf");

    private String filenameFor(String dtxsid, String sessionKey, String date) {
        return dtxsid + "_LANDSCAPE_" + sessionKey + "_" + date + ".pdf";
    }

    private String generateUploadUrl() {
        return String.format("%s/uploadToDataset/%s?key=%s", clowderBaseUrl, dataset, apiKey);
    }

    private LandscapeReport landscapeReportFor(String dtxsid, String sessionKey, String clowderId, String username, int newVersion, String workflow) {
        LandscapeReport landscapeReport = new LandscapeReport();
        landscapeReport.setDtxsid(dtxsid);
        landscapeReport.setSessionKey(sessionKey);
        landscapeReport.setGenerated(true);
        landscapeReport.setClowderId(clowderId);
        landscapeReport.setCreatedBy(username);
        landscapeReport.setGenerate(false);
        landscapeReport.setVersion(newVersion);
        landscapeReport.setWorkflow(workflow);
        return landscapeReport;
    }

    @Transactional
    public String generateReportAndUploadToClowder(GenerateLandscapeReportRequest req) throws IOException {
        byte[] bytes = generateReport(req);
        if (bytes == null || bytes.length == 0) {
            log.warn("landscape report generation returned a null or empty report");
            return null;
        }
        return exportReportToClowder(req.getDtxsid(), req.getWorkflow(), bytes, req.getSessionKey(), req.getUsername());
    }

    public byte[] generateReport(GenerateLandscapeReportRequest landscapeGenerateChemical) {
        LandscapeReportData reportData = gatherLandscapeReportData(landscapeGenerateChemical);
        return reportGenerator.generateLandscapeReport(reportData);
    }

    private LandscapeReportData gatherLandscapeReportData(GenerateLandscapeReportRequest request) {
        final String dtxsid = request.getDtxsid();
        final String workflow = request.getWorkflow();

        return LandscapeReportData.builder()
                .dtxsid(dtxsid)
                .workflow(workflow)
                .preferredName("preferred_name")
                .casrn("casrn")
                .ccdLink("ccdLink")
                .hazardData(gatherLandscapeHazardData(dtxsid, workflow))
                .physchemData(gatherPhyschemData(dtxsid))
                .odorThresholdData(gatherOdorThresholdData(dtxsid))
                .bioactivityData(gatherBioactivityData(dtxsid))
                .build();
    }

    private ReportPhyschemData gatherPhyschemData(String dtxsid) {
        List<PhyschemSummary> pData = physchemRepo.findByDtxsidAndPropertyInOrderByPropertyAsc(dtxsid, PHYSCHEM_PROPERTY_NAMES);
        List<PhyschemSummary> eData = physchemRepo.findByDtxsidAndPropertyInOrderByPropertyAsc(dtxsid, ENVFATE_PROPERTY_NAMES);

        return ReportPhyschemData.builder()
                .physchems(pData.stream().map(ReportPhyschem.mapper()::map).collect(Collectors.toList()))
                .envFates(eData.stream().map(ReportPhyschem.mapper()::map).collect(Collectors.toList()))
                .build();
    }

    private ReportHazardData gatherLandscapeHazardData(String dtxsid, String workflow) {
        var hazards = hazardRepo.findAllByDtxsidAndWorkflow(dtxsid, workflow).stream()
                .sorted(new HazardComparator())
                .map(ReportHazard.hazardMapper()::map)
                .collect(Collectors.toList());

        return ReportHazardData.build(hazards);
    }

    private ReportOdorThresholdData gatherOdorThresholdData(String dtxsid) {
        Optional<OdorThreshold> o = odorRepo.findByDtxsidAndOdorThresholdIsNotNull(dtxsid);
        if (o.isPresent()) {
            ReportOdorThreshold ro = ReportOdorThreshold.mapper().map(o.get());
            return ReportOdorThresholdData.builder().odorThresholds(List.of(ro)).build();
        } else {
            return ReportOdorThresholdData.builder().odorThresholds(Collections.emptyList()).build();
        }
    }

    private ReportBioactivityData gatherBioactivityData(String dtxsid) {
        return ReportBioactivityData.builder()
                .activitySummary(gatherActivitySummary(dtxsid))
                .berSummary(gatherBERData(dtxsid))
                .models(gatherBioactivityModels(dtxsid))
                .build();
    }

    private List<ReportBioactivityBERSummary> gatherBERData(String dtxsid) {
        final Optional<BioactivityBerSummary> berSummary = bioService.fetchBioactivityBerSummariesForDtxsids(List.of(dtxsid)).stream().findFirst();
        if (berSummary.isPresent()) {
            return List.of(ReportBioactivityBERSummary.mapper()
                    .map(berSummary.get()));
        } else {
            return Collections.emptyList();
        }
    }

    private List<ReportBioactivitySummary> gatherActivitySummary(String dtxsid) {
        final Optional<BioactivitySummary> bioactivitySummary = bioService.fetchBioactivitySummaryForDtxsid(dtxsid);
        if (bioactivitySummary.isPresent()) {
            return List.of(ReportBioactivitySummary.mapper()
                    .map(bioactivitySummary.get()));
        } else {
            return Collections.emptyList();
        }
    }

    private List<ReportBioactivityModel> gatherBioactivityModels(String dtxsid) {
        return bioactivityModelRepo.findByDtxsid(dtxsid).stream()
                .map(ReportBioactivityModel.mapper()::map)
                .collect(Collectors.toList());
    }

}
