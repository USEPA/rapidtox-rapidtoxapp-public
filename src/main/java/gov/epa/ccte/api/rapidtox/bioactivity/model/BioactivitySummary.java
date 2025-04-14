package gov.epa.ccte.api.rapidtox.bioactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Data
public class BioactivitySummary {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("dtxsid")
    private String dtxsid;

    @JsonProperty("chemicalName")
    private String chemicalName;

    @JsonProperty("inactiveCount")
    private Integer inactiveCount;

    @JsonProperty("activeCount")
    private Integer activeCount;

    @JsonProperty("totalAssaysScreened")
    private Integer totalAssaysScreened;

    @JsonProperty("hitRate")
    private Double hitRate;

	@JsonProperty("invitroPod05")
	private Double invitroPod05;
	
    @JsonProperty("aed50Pctile05")
    private Double aed50Pctile05;

    @JsonProperty("aed95Pctile05")
    private Double aed95Pctile05;

    @JsonProperty("invitroVersion")
    private String invitroVersion;

}
