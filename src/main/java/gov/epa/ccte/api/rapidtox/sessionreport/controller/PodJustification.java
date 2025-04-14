package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PodJustification {

    private Integer podId;
    private String dtxsid;
    private String justification;
}
