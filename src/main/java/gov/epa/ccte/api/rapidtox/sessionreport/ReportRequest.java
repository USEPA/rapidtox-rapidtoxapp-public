package gov.epa.ccte.api.rapidtox.sessionreport;

import lombok.Data;

@Data
public class ReportRequest {

    private String username;
    private String dtxsid;
    private String workflow;
    private String workflowTitle;
    private String sessionKey;
    private String reportDesc;
    private String safetyLink;
    private Boolean hasHazardToxData;
    private Boolean hasHazardPodData;
    private Boolean hasPhyschemData;
    private Boolean hasEnvData;
    private Boolean hasToxcastData;
    private Boolean isBioactivitySummarySelected;
    private Boolean hasBerData;
    private ReportRequestDetails data;
    private String section;
    private String chartType;
    private Integer width;
    private Integer height;
}
