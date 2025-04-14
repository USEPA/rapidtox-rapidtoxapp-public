package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnalogueDetails {

    private String chemicalName;
    private String dtxsid;
    private Integer[] podIds;
}
