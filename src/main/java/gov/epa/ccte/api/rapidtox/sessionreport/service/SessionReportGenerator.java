package gov.epa.ccte.api.rapidtox.sessionreport.service;

import gov.epa.ccte.api.rapidtox.util.ZipHelper;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardClassificationService;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardDTOComparator;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardDtoPlotItemMapper;
import gov.epa.ccte.api.rapidtox.bioactivity.service.BioactivityService;
import gov.epa.ccte.api.rapidtox.chemical.ChemicalHeaderInfo;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.HazardClassificationTuple;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportAnalogueDetails;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import gov.epa.ccte.api.rapidtox.config.RapidToxConstants;
import gov.epa.ccte.api.rapidtox.chemical.model.ChemicalDetails;
import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.sessionreport.CustomHazardToHazardDtoMapper;
import gov.epa.ccte.api.rapidtox.sessionreport.HazardDTO;
import gov.epa.ccte.api.rapidtox.sessionreport.HazardToHazardDtoMapper;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivitySummary;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportHazardData;
import gov.epa.ccte.api.rapidtox.chemical.repository.ChemicalDetailsRepository;
import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.HazardPlotItem;
import gov.epa.ccte.api.rapidtox.clowder.service.ClowderDetailsService;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionReportGenerator {

    private static final String SINGLE_REPORT_PATH = "/single-chemical-report";

    public static String getSingleReportPath() {
        return SINGLE_REPORT_PATH;
    }
    
    @Qualifier("primaryDataSource")
    private final DataSource primaryDataSource;

    @Value("${clowder.base-url}")
    private String clowderBaseUrl;

    private final ApplicationContext appCtx;
    private final HazardRepository hazardRepo;
    private final ChemicalDetailsRepository chemicalDetailRepo;
    private final BioactivityService bioactivityService;
    private final HazardClassificationService classificationService;
    private final ScatterPlotFetcher scatterPlotFetcher;
    private final CustomHazardToHazardDtoMapper customToHazardMapper;
    private final HazardToHazardDtoMapper hazardMapper;
    private final HazardDtoPlotItemMapper hazardDtoPlotItemMapper;

    private static String getTimezoneAbbrev(String localZoneId) {
        TimeZone tz = TimeZone.getTimeZone(localZoneId);
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    public JasperReport fetchReportFor(final String reportRootPath, final String reportFilename) throws JRException, IOException {
        //Get report files and compile the reports
        Resource resource = appCtx.getResource("classpath:" + reportRootPath + "/" + reportFilename);
        return JasperCompileManager.compileReport(resource.getInputStream());
    }

    public byte[] generateSingleReport(ReportRequest reportRequest) throws Exception {

        final DataSource ds = primaryDataSource;
        Connection conn = null;
        try {
            log.info("datasource type: {}", ds.getClass().getName());
            conn = DataSourceUtils.getConnection(ds);
            log.info("connection type: {}", conn.getClass().getName());
            return generateSingleReport(reportRequest, conn);
        } catch (SQLException e) {
            log.error("error {}", e);
            return null;
        } finally {
            DataSourceUtils.releaseConnection(conn, ds);
        }
    }

    public byte[] generateSingleReport(ReportRequest reportRequest, Connection dbConnection) throws Exception {
        try {

            final String requestedDtxsid = reportRequest.getDtxsid();
            final String workflow = reportRequest.getWorkflow();

            //add custom report data
            Set<Integer> selectedHazardIds = new TreeSet<>(reportRequest.getData().getHazard());

            List<Integer> selectedPhyschemList = new ArrayList<>(Arrays.asList(reportRequest.getData().getPhyschem()));

            List<Integer> selectedToxcastList = new ArrayList<>(Arrays.asList(reportRequest.getData().getToxcast()));

            ReportAnalogueDetails analogueDetails = reportRequest.getData().getReportAnalogueDetails();

            List<Hazard> availableHazards = hazardRepo.findAllByDtxsidAndWorkflow(requestedDtxsid, workflow);
            List<Hazard> selectedHazards = availableHazards.stream()
                    .filter(h -> selectedHazardIds.contains(h.getId()))
                    .collect(Collectors.toList());
            List<HazardDTO> availableHazardDTOs = availableHazards.stream()
                    .map(hazardMapper::map)
                    .collect(Collectors.toList());
            List<HazardDTO> selectedHazardDTOs = selectedHazards.stream()
                    .map(hazardMapper::map)
                    .collect(Collectors.toList());

            // merge custom data in with selected hazards
            // since all custom data items come in a single array, 
            // we also need to filter out custom data that doesn't match 
            // our current chemical while also ensuring that its in 
            // marked as selected 
            List<HazardDTO> customSelectedHazards = reportRequest.getData().getCustomData().stream()
                    .filter(h -> h.getDtxsid().equals(requestedDtxsid))
                    .filter(h -> selectedHazardIds.contains(h.getId()))
                    .map(in -> {
                        HazardDTO result = customToHazardMapper.map(in);
                        if (in.getOriginalHazardId() != null) {
                            availableHazardDTOs.stream()
                                    .filter(h -> h.getId().equals(in.getOriginalHazardId()))
                                    .findFirst()
                                    .ifPresent(t -> {
                                        result.setOriginalValue(t.getToxvalNumeric());
                                        result.setOriginalUnits(t.getToxvalUnits());
                                    });
                        }
                        return result;
                    })
                    .collect(Collectors.toList());
            selectedHazardDTOs.addAll(customSelectedHazards);

            // ensure they're ordered just so
            selectedHazardDTOs.sort(new HazardDTOComparator());

            final List<ReportHazard> availableReportHazards = availableHazardDTOs.stream().map(ReportHazard.hazardDtoMapper()::map).collect(Collectors.toList());
            final List<ReportHazard> selectedReportHazards = selectedHazardDTOs.stream().map(ReportHazard.hazardDtoMapper()::map).collect(Collectors.toList());
            final ReportHazardData hazardData = ReportHazardData.build(availableReportHazards, selectedReportHazards);

            // get stuff to make the report work
            ChemicalDetails cd = chemicalDetailRepo.findByDtxsid(requestedDtxsid);
            List<ChemicalHeaderInfo> chemicals = List.of(ChemicalHeaderInfo.builder()
                    .casrn(cd.getCasrn())
                    .dtxsid(cd.getDtxsid())
                    .preferredName(cd.getPreferredName())
                    .build());

            // If any list is empty add -1 value to help with IN operator filtering
            if (selectedHazardIds.isEmpty()) {
                selectedHazardIds.add(-1);
            }

            if (selectedPhyschemList.isEmpty()) {
                selectedPhyschemList.add(-1);
            }

            if (selectedToxcastList.isEmpty()) {
                selectedToxcastList.add(-1);
            }

            if (analogueDetails.getPodIdsList().isEmpty()) {
                ArrayList<Integer> blankPodsList = new ArrayList<>();
                blankPodsList.add(-1);
                analogueDetails.setPodIdsList(blankPodsList);
            }

            // setup bioactivity summary 
            List<ReportBioactivitySummary> selectedBioactivitySummary = Collections.emptyList();
            Optional<ReportBioactivitySummary> availableBioactivitySummaries = buildBioactivitySummaryReportData(requestedDtxsid);
            if (reportRequest.getIsBioactivitySummarySelected()) {
                selectedBioactivitySummary = List.of(availableBioactivitySummaries.get());
            }

            // new image mechanism
            final Double predictedExposure = reportRequest.getData().getPredictedExposure();
            final List<HazardPlotItem> inVivoPlotItems = selectedHazardDTOs.stream()
                    .map(hazardDtoPlotItemMapper::map)
                    .toList();
            final BufferedImage inVivoToxOralScatterPlot = scatterPlotFetcher.fetchScatterPlotImageFor(inVivoPlotItems, "oral", predictedExposure);
            final BufferedImage inVivoToxInhalationScatterPlot = scatterPlotFetcher.fetchScatterPlotImageFor(inVivoPlotItems, "inhalation", predictedExposure);

            final String landscapeReportUrl = generateLandscapeReportUrl(requestedDtxsid, workflow);

            Map<String, Object> parameters = initializeSingleReportParameters();

            // inject data parameters
            parameters.put("dtxsid", requestedDtxsid);
            parameters.put("casrn", cd.getCasrn());
            parameters.put("preferredName", cd.getPreferredName());
            parameters.put("workflow", workflow);
            parameters.put("workflowTitle", reportRequest.getWorkflowTitle());
            parameters.put("sessionKey", reportRequest.getSessionKey());
            parameters.put("username", reportRequest.getUsername());
            parameters.put("safetyLink", reportRequest.getSafetyLink());
            parameters.put("landscapeReportUrl", landscapeReportUrl);
            parameters.put("customData", filterCustomReportDataByDtxsid(reportRequest));
            parameters.put("hasCustomData", !filterCustomReportDataByDtxsid(reportRequest).isEmpty());
            parameters.put("selectedHazardData", hazardData.getSelectedHazards());
            parameters.put("hazardSelections", selectedHazardIds);
            parameters.put("hazardSuperCategories", hazardData.getSuperCategories());
            parameters.put("physchemSelections", selectedPhyschemList);
            parameters.put("physchemFilterSelections", RapidToxConstants.PHYSCHEM_PROPERTY_NAMES);
            parameters.put("envfateFilterSelections", RapidToxConstants.ENVFATE_PROPERTY_NAMES);
            parameters.put("toxcastSelections", selectedToxcastList);
            parameters.put("analogueSelections", analogueDetails);
            parameters.put("isBioactivitySummaryDataPresent", availableBioactivitySummaries.isPresent());
            parameters.put("bioactivitySummary", selectedBioactivitySummary);
            parameters.put("inVivoToxicityOralScatterPlot", inVivoToxOralScatterPlot);
            parameters.put("inVivoToxicityInhalationScatterPlot", inVivoToxInhalationScatterPlot);
            // and add the big blob of data that is the request
            parameters.put("reportRequest", reportRequest);

            parameters.put("REPORT_CONNECTION", dbConnection);

            JasperReport masterReport = fetchReportFor(SINGLE_REPORT_PATH, "Single_Chemical_Report.jrxml");
            //Create the final master report and export to a PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parameters, new JRBeanCollectionDataSource(chemicals));

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private String generateLandscapeReportUrl(final String requestedDtxsid, final String workflow) {
        return String.format("%s/files/%s", clowderBaseUrl,
                ClowderDetailsService.getClowderIdWithReadKey(requestedDtxsid, workflow));
    }

    private Map<String, Object> initializeSingleReportParameters() throws JRException, IOException {
        Map<String, Object> parameters = initializeReportParametersMap();
        // subreport injections
        parameters.put("reportInformation", fetchReportFor(SINGLE_REPORT_PATH, "report_information.jrxml"));
        parameters.put("physchemSubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_physchem_report.jrxml"));
        parameters.put("envfateSubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_envfate_report.jrxml"));
        parameters.put("odorThresholdSubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_odor_threshold_report.jrxml"));
        parameters.put("hazardReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_hazard_report.jrxml"));
        parameters.put("hazardDetailReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_hazard_detail_report.jrxml"));
        parameters.put("hazardAvailableDataReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_hazard_classification_detail_report.jrxml"));
        parameters.put("analogueSubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_analogue_report.jrxml"));
        parameters.put("toxcastSubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_toxcast_report.jrxml"));
        parameters.put("bioactivitySummarySubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_bioactivity_summary_report.jrxml"));
        parameters.put("berSubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_ber_report.jrxml"));
        parameters.put("uncertainitySubReport", fetchReportFor(SINGLE_REPORT_PATH, "scr_pod_uncertainity_report.jrxml"));
        parameters.put("uncertainitySubReportSec", fetchReportFor(SINGLE_REPORT_PATH, "scr_pod_uncertainity_sec_report.jrxml"));
        return parameters;
    }

    private Map<String, Object> initializeReportParametersMap() {
        // Create the report parameters
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("timeZone", getTimezoneAbbrev(ZoneId.systemDefault().toString()));
        parameters.put("noStructureImage", noStructureImage);
        return parameters;
    }

    private List<HazardClassificationTuple> buildStudyAvailableData(List<Hazard> selectedHazards) {
        final Map<Integer, Set<String>> hazardClassifications = classificationService.classify(selectedHazards);
        final Set<String> requiredClassifications = new LinkedHashSet<>(List.of("developmental", "reproductive", "immunotoxicity", "neurotoxicity", "endocrine", "respiratory"));
        final List<HazardClassificationTuple> finalClassifications = new ArrayList<>();
        for (String req : requiredClassifications) {
            final HazardClassificationTuple tuple = new HazardClassificationTuple(req, "No");
            for (Set<String> cls : hazardClassifications.values()) {
                if (cls.contains(req)) {
                    tuple.setAvailableStudyInformation("Yes");
                }
            }
            finalClassifications.add(tuple);
        }
        return finalClassifications;
    }

    private static final BufferedImage noStructureImage;

    static {
        try (InputStream inputStream = SessionReportGenerator.class.getResourceAsStream("/images/NoStructureImage.jpg")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: /images/NoStructureImage.jpg");
            }
            noStructureImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("unable to load no-structure image from disk", e);
        }
    }

    public byte[] generateBatchReport(ReportRequest[] reportRequests) throws Exception {
        final DataSource ds = primaryDataSource;
        log.info("datasource type: {}", ds.getClass().getName());
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(ds);

            //initialize zipOutputStream and variables necessary for writing and deleting files
            byte[] output = null;
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
            String reportName = "batch_report_" + reportRequests[0].getSessionKey() + "_" + sdf.format(date) + ".zip";
            FileOutputStream fos = new FileOutputStream(reportName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //generate individual pdfs for each reportRequest
            for (int i = 0; i < reportRequests.length; i++) {
                byte[] file = generateSingleReport(reportRequests[i], conn);
                try {
                    //temporary file created from byte array, added to zip file, and deleted
                    File tempJavaFile = new File(reportRequests[i].getDtxsid() + "_" + reportRequests[i].getSessionKey() + ".pdf");
                    Files.write(tempJavaFile.toPath(), file);
                    ZipHelper.addToZipFile(tempJavaFile.toString(), zos);
                    tempJavaFile.delete();
                } catch (Exception e) {
                    fos.close();
                    zos.close();
                }
            }
            //close zip writing utilities
            zos.close();
            fos.close();
            //initialize fileinputstream to retrieve byte array from zip file
            File file = new File(reportName);
            FileInputStream fl = new FileInputStream(file);
            output = fl.readAllBytes();
            //closefile input stream and delete zip file
            fl.close();
            file.delete();

            return output;
        } catch (Exception e) {
            log.error("error generating batch report {}", e);
            return null;
        } finally {
            DataSourceUtils.releaseConnection(conn, ds);
        }
    }

    private List<CustomHazard> filterCustomReportDataByDtxsid(ReportRequest reportRequest) {
        return reportRequest.getData().getCustomData().stream()
                .filter(item -> item.getDtxsid().equalsIgnoreCase(reportRequest.getDtxsid()))
                .toList();
    }

    Optional<ReportBioactivitySummary> buildBioactivitySummaryReportData(String dtxsid) {
        return bioactivityService.fetchBioactivitySummaryForDtxsid(dtxsid).map(ReportBioactivitySummary.MAPPER::map);
    }

}
