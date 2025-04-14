package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HazardClassificationTuple {
    String classificationName;
    String availableStudyInformation;
}
