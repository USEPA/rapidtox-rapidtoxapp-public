package gov.epa.ccte.api.rapidtox.bioactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "invitrodb_summary", schema = "rapidtox")
@Data
public class InvitroDbSummary {

	@Id
	@Column(name = "id")
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("dtxsid")
	@Column(name = "dtxsid")
	private String dtxsid;

	@JsonProperty("chemicalName")
	@Column(name = "chemical_name")
	private String chemicalName;

	@JsonProperty("inactiveCount")
	@Column(name = "inactive_count")
	private Integer inactiveCount;

	@JsonProperty("activeCount")
	@Column(name = "active_count")
	private Integer activeCount;

	@JsonProperty("totalAssaysScreened")
	@Column(name = "total_assays_screened")
	private Integer totalAssaysScreened;

	@JsonProperty("hitRate")
	@Column(name = "hit_rate")
	private Double hitRate;

	@JsonProperty("aed50Pctile05")
	@Column(name = "aed50_pctile_05")
	private Double aed50Pctile05;

	@JsonProperty("aed50Pctile95")
	@Column(name = "aed50_pctile_95")
	private Double aed50Pctile95;

	@JsonProperty("aed50Pctile50")
	@Column(name = "aed50_pctile_50")
	private Double aed50Pctile50;

	@JsonProperty("aed95Pctile05")
	@Column(name = "aed95_pctile_05")
	private Double aed95Pctile05;

	@JsonProperty("aed95Pctile50")
	@Column(name = "aed95_pctile_50")
	private Double aed95Pctile50;

	@JsonProperty("aed95Pctile95")
	@Column(name = "aed95_pctile_95")
	private Double aed95Pctile95;

	@JsonProperty("aedUnit")
	@Column(name = "aed_unit")
	private String aedUnit;

	@JsonProperty("berLowerbound")
	@Column(name = "ber_lowerbound")
	private Double berLowerbound;

	@JsonProperty("berMedian")
	@Column(name = "ber_median")
	private Double berMedian;

	@JsonProperty("invitroPod05")
	@Column(name="invitro_pod_05")
	private Double invitroPod05;
	
	@JsonProperty("modelParam")
	@Column(name = "model_param")
	private String modelParam;

	@JsonProperty("invitroVersion")
	@Column(name = "invitro_version")
	private String invitroVersion;

	@JsonProperty("exposureMedian")
	@Column(name = "seem3_median")
	private Double exposureMedian;

	@JsonProperty("exposure95PercentMedian")
	@Column(name = "seem3_percentile95")
	private Double exposure95PercentMedian;
}
