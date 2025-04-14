package gov.epa.ccte.api.rapidtox.bioactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BioactivityBerSummary {

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

	@JsonProperty("aed50Pctile05")
	private Double aed50Pctile05;

	@JsonProperty("aed50Pctile95")
	private Double aed50Pctile95;

	@JsonProperty("aed50Pctile50")
	private Double aed50Pctile50;

	@JsonProperty("aed95Pctile05")
	private Double aed95Pctile05;

	@JsonProperty("aed95Pctile50")
	private Double aed95Pctile50;

	@JsonProperty("aed95Pctile95")
	private Double aed95Pctile95;

	@JsonProperty("aedUnit")
	private String aedUnit;

	@JsonProperty("berLowerbound")
	private Double berLowerbound;

	@JsonProperty("berMedian")
	private Double berMedian;

	@JsonProperty("modelParam")
	private String modelParam;

	@JsonProperty("invitroVersion")
	private String invitroVersion;

	@JsonProperty("exposureMedian")
	private Double exposureMedian;

	@JsonProperty("exposure95PercentMedian")
	private Double exposure95PercentMedian;
}
