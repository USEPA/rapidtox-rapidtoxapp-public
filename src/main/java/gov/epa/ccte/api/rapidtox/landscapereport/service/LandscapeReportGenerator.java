package gov.epa.ccte.api.rapidtox.landscapereport.service;

import gov.epa.ccte.api.rapidtox.hazard.service.HazardPlotItemMapper;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardClassificationService;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardDTOComparator;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardDtoPlotItemMapper;
import gov.epa.ccte.api.rapidtox.bioactivity.service.BioactivityService;
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
import gov.epa.ccte.api.rapidtox.landscapereport.LandscapeReportData;
import gov.epa.ccte.api.rapidtox.chemical.repository.ChemicalDetailsRepository;
import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.HazardPlotItem;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LandscapeReportGenerator {

    static final String LANDSCAPE_REPORT_ROOT = "/landscape-report";

    @Qualifier("primaryDataSource")
    private final DataSource primaryDataSource;

    private final ApplicationContext appCtx;

    private static String getTimezoneAbbrev(String localZoneId) {
        TimeZone tz = TimeZone.getTimeZone(localZoneId);
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    public JasperReport fetchReportFor(final String reportRootPath, final String reportFilename) throws JRException, IOException {
        //Get report files and compile the reports
        Resource resource = appCtx.getResource("classpath:" + reportRootPath + "/" + reportFilename);
        return JasperCompileManager.compileReport(resource.getInputStream());
    }


    private Map<String, Object> initializeReportParametersMap() {
        // Create the report parameters
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("timeZone", getTimezoneAbbrev(ZoneId.systemDefault().toString()));
        parameters.put("noStructureImage", noStructureImage);
        return parameters;
    }

    public byte[] generateLandscapeReport(LandscapeReportData reportData) {
        try {
            JasperReport masterReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "Landscape_Report.jrxml");

            Map<String, Object> parameters = initializeLandscapeReportParameters();

            //Create the report parameters
            parameters.put("landscapeReportData", reportData);
            parameters.put("dtxsid", reportData.getDtxsid());
            parameters.put("workflow", reportData.getWorkflow());
            parameters.put("hazardData", reportData.getHazardData().getHazards());
            parameters.put("hazardSuperCategories", reportData.getHazardData().getSuperCategories());

            final DataSource ds = primaryDataSource;
            Connection conn = null;
            try {
                conn = DataSourceUtils.getConnection(ds);

                JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parameters, conn);

                return JasperExportManager.exportReportToPdf(jasperPrint);

            } catch (JRException | CannotGetJdbcConnectionException e) {
                log.error("trouble generating landscape report {}", e);
                return null;
            } finally {
                DataSourceUtils.releaseConnection(conn, ds);
            }
        } catch (IOException | JRException ex) {
            log.error("trouble generating landscape report", ex);
            return null;
        }
    }
    private static final BufferedImage noStructureImage;

    static {
        try (InputStream inputStream = LandscapeReportGenerator.class.getResourceAsStream("/images/NoStructureImage.jpg")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: /images/NoStructureImage.jpg");
            }
            noStructureImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("unable to load no-structure image from disk", e);
        }
    }

    private Map<String, Object> initializeLandscapeReportParameters() throws JRException, IOException {
        // Get report files and compile the reports
        Map<String, Object> parameters = initializeReportParametersMap();
        // subreport injections
        JasperReport physchemSubReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_physchem_report.jrxml");
        JasperReport envFateSubReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_envfate_report.jrxml");
        JasperReport odorThresholdSubReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_odor_threshold_report.jrxml");
        JasperReport hazardReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_hazard_report.jrxml");
        JasperReport hazardDetailReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_hazard_detail_report.jrxml");
        JasperReport bioBerSummaryReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_bioactivity_ber_summary_report.jrxml");
        JasperReport bioSummaryReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_bioactivity_summary_report.jrxml");
        JasperReport bioModelReport = fetchReportFor(LANDSCAPE_REPORT_ROOT, "landscape_bioactivity_model_report.jrxml");
        parameters.put("physchemSubReport", physchemSubReport);
        parameters.put("envFateSubReport", envFateSubReport);
        parameters.put("odorThresholdSubReport", odorThresholdSubReport);
        parameters.put("hazardReport", hazardReport);
        parameters.put("hazardDetailReport", hazardDetailReport);
        parameters.put("bioSummaryReport", bioSummaryReport);
        parameters.put("bioBerSummaryReport", bioBerSummaryReport);
        parameters.put("bioModelReport", bioModelReport);
        return parameters;
    }

}
