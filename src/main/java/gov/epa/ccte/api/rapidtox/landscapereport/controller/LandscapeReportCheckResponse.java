package gov.epa.ccte.api.rapidtox.landscapereport.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LandscapeReportCheckResponse {

	@JsonProperty("dtxsid")
	private String dtxsid;

	@JsonProperty("preferredName")
	private String preferredName;

	@JsonProperty("workflow")
	private String workflow;

}
