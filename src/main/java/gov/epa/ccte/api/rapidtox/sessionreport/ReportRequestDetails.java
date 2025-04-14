package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.sessionreport.controller.BerData;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.PodDataSelected;
import java.util.List;
import lombok.Data;

@Data
public class ReportRequestDetails {

    private List<Integer> hazard;

    private Integer[] physchem;

    private Integer[] toxcast;

    private ReportAnalogueDetails reportAnalogueDetails;

    private List<PodDataSelected> podDataSelected;

    private List<BerData> ber;

    private List<CustomHazard> customData;
    
    private List<String> selectedBioactivitySummaryChemicals;

    private Double predictedExposure;
}
