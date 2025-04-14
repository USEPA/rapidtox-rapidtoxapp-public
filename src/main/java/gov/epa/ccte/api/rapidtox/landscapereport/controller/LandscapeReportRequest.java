package gov.epa.ccte.api.rapidtox.landscapereport.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LandscapeReportRequest {

	@JsonProperty("dtxsids")
	private List<String> dtxsids;

	@JsonProperty("sessionKey")
	private String sessionKey;

	@JsonProperty("workflow")
	private String workflow;
}
