package gov.epa.ccte.api.rapidtox.hazard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;

@Data
public class SimilarPodAggregator {

    @Column(name = "dtxsid")
    @JsonProperty("dtxsid")
    private String dtxsid;

    @Column(name = "casrn")
    @JsonProperty("casrn")
    private String casrn;

    @Column(name = "preferred_name")
    @JsonProperty("preferredName")
    private String preferredName;

    @Column(name = "iris_total")
    @JsonProperty("irisTotal")
    private Integer irisTotal;

    @Column(name = "pprtv_total")
    @JsonProperty("pprtvTotal")
    private Integer pprtvTotal;

    @Column(name = "atsdr_total")
    @JsonProperty("atsdrTotal")
    private Integer atsdrTotal;

    @Column(name = "opp_total")
    @JsonProperty("oppTotal")
    private Integer oppTotal;

    @Column(name = "other_total")
    @JsonProperty("otherTotal")
    private Integer otherTotal;

    @JsonProperty("similarity")
    private Double similarity;

    @Transient
    private Boolean isTarget = false;

}
