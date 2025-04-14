package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomHazard {

	private Integer id;
    @JsonProperty(required = false)
    private Integer originalHazardId;
	private String dtxsid;
	private String casrn;
	private String preferredName;
	private String toxvalType;
	private String toxvalSubtype;
	private String riskAssessmentClass;
	private Double toxvalNumeric;
	private String toxvalUnits;
	private String studyType;
	private Double studyDuration;
	private String studyDurationUnits;
	private String exposureRoute;
	private String exposureMethod;
    @JsonProperty("speciesCommon")
	private String species;
	private String source;
	private String effect;
	private String toxvalTypDefn;
	private String superCategory;
	private String superSource;
}
