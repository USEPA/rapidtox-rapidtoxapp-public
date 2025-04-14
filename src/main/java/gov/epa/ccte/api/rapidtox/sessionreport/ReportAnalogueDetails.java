package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.sessionreport.controller.PodJustification;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportAnalogueDetails {

    private List<Integer> podIdsList;
    private List<String> dtxsidList;
    private List<Double> similarityList;
    private List<PodJustification> podJustifications;
}
