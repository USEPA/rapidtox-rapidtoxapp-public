package gov.epa.ccte.api.rapidtox.landscapereport.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateLandscapeReportRequest {

    @JsonProperty("dtxsid")
    private String dtxsid;

	@JsonProperty("workflow")
	private String workflow;
	
    @JsonProperty("sessionKey")
    private String sessionKey;

    @JsonProperty("username")
    private String username;
}
