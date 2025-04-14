package gov.epa.ccte.api.rapidtox.service;

import gov.epa.ccte.api.rapidtox.sessionreport.service.SessionReportGenerator;
import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivitySummary;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.HazardClassificationTuple;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

public class ReportTests {

    @Test
    public void givenHazardData_shouldProduceReportWithData() throws Exception {
        File reportFile = ResourceUtils.getFile(SessionReportGenerator.class.getResource(SessionReportGenerator.getSingleReportPath()).toURI() + "/scr_hazard_detail_report.jrxml");
        JasperReport report = JasperCompileManager.compileReport(reportFile.getAbsolutePath());

        Map<String, Object> parameters = new HashMap<>();

        List<Hazard> hazards = new ArrayList<>();

        final String dtxsid = HazardDataGenerator.newDTXSID(1);

        for (int i = 0; i < 10; i++) {
            hazards.add(
                    HazardDataGenerator.randomHazard(dtxsid)
            );
        }

        final String subReportHeader = "In Vivo Toxicity Values";
        if(hazards.stream().filter(h->h.getSuperCategory().equals(subReportHeader)).findAny().isEmpty()) {
            hazards.get(0).setSuperCategory(subReportHeader);
        }
        
        parameters.put("dtxsid", dtxsid);
        parameters.put("superCategoryDataAvailable", true);
        parameters.put("superCategoryHeader", subReportHeader);
        
        List<ReportHazard> reportHazards = hazards.stream().map(h -> ReportHazard.hazardMapper().map(h)).collect(Collectors.toList());
        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(reportHazards));

        JasperExportManager.exportReportToPdfFile(print, "output-with-data.pdf");

        print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(null));

        JasperExportManager.exportReportToPdfFile(print, "output-without-selections.pdf");

        parameters.put("superCategoryDataAvailable", false);

        print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(null));

        JasperExportManager.exportReportToPdfFile(print, "output-without-data.pdf");

    }

    @Test
    void givenHazardClassificationData_shouldGenerateReport() throws Exception {
        List<HazardClassificationTuple> tuples
                = List.of(
                        new HazardClassificationTuple("endocrine", "yes"),
                        new HazardClassificationTuple("developmental", "no"));

        File reportFile = ResourceUtils.getFile(SessionReportGenerator.class.getResource(SessionReportGenerator.getSingleReportPath()).toURI() + "/scr_hazard_classification_detail_report.jrxml");
        JasperReport report = JasperCompileManager.compileReport(reportFile.getAbsolutePath());

        Map<String, Object> parameters = new HashMap<>();

        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(tuples));

        JasperExportManager.exportReportToPdfFile(print, "hazard-classification-detail.pdf");
    }

    @Test
    public void givenBioactivitySummaryData_shouldGenerateReport() throws Exception {
        final String bioActSumDataPresentKey = "isBioactivitySummaryDataPresent";
        
        File reportFile = ResourceUtils.getFile(SessionReportGenerator.class.getResource(SessionReportGenerator.getSingleReportPath()).toURI() + "/scr_bioactivity_summary_report.jrxml");

        JasperReport report = JasperCompileManager.compileReport(reportFile.getAbsolutePath());

        List<ReportBioactivitySummary> data = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        
        parameters.put(bioActSumDataPresentKey, false);
        
        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(data));

        JasperExportManager.exportReportToPdfFile(print, "bioactivity-summary-section-without-data.pdf");
        
        parameters.put(bioActSumDataPresentKey, true);
        
        print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(data));

        JasperExportManager.exportReportToPdfFile(print, "bioactivity-summary-section-with-unselected-data.pdf");

        ReportBioactivitySummary bioSum = ReportBioactivitySummary.builder()
                .inactiveCount("730")
                .activeCount("221")
                .totalAssaysScreened("951")
                .hitRate(String.valueOf(Math.round(10000.0 * 221 / 951) / 100.0))
                .aed50pctile05(String.valueOf(0.390))
                .aed50pctile95(String.valueOf(0.123))
                .invitroVersion("77")
                .build();

        data = List.of(bioSum);
        print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(data));

        JasperExportManager.exportReportToPdfFile(print, "bioactivity-summary-section-with-selected-data.pdf");

    }

}
