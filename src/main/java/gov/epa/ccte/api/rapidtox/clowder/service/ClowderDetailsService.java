package gov.epa.ccte.api.rapidtox.clowder.service;

import gov.epa.ccte.api.rapidtox.landscapereport.model.LandscapeReport;
import gov.epa.ccte.api.rapidtox.landscapereport.repository.LandscapeReportRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Component
@Transactional
public class ClowderDetailsService {

    @Value("${clowder.read-key}")
    private String readKey;

    private static String READ_KEY_STATIC;

    @Value("${clowder.read-key}")
    public void setNameStatic(String readKey) {
        ClowderDetailsService.READ_KEY_STATIC = readKey;
    }

    private static LandscapeReportRepository landscapeReportRepository;

    @Autowired
    public ClowderDetailsService(LandscapeReportRepository landscapeReportRepository) {
        ClowderDetailsService.landscapeReportRepository = landscapeReportRepository;

    }


    public static String getClowderIdWithReadKey(String dtxsid, String workflow) {
		Assert.notNull(dtxsid, "dtxsid cannot be null");
		Assert.notNull(workflow, "workflow cannot be null");
        LandscapeReport landscapeReport = landscapeReportRepository.findFirstByDtxsidAndWorkflowOrderByVersionDesc(dtxsid, workflow);
		Assert.notNull(landscapeReport, "retrieved landscape report cannot be null");
        return landscapeReport.getClowderId() + "/blob?key=" + READ_KEY_STATIC;
    }

}
