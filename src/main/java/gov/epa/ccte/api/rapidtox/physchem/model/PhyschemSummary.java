package gov.epa.ccte.api.rapidtox.physchem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author asif
 * Create at 2021-06-01 10:22
 */
@Entity
@Table(name = "physchem_summary", schema = "rapidtox")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhyschemSummary {

    /**
     *
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     *
     */
    @Column(name = "dtxsid", length = 45)
    @JsonProperty("dtxsid")
    private String dtxsid;

    /**
     *
     */
    @Column(name = "preferred_name")
    @JsonProperty("preferredName")
    private String preferredName;

    /**
     *
     */
    @Column(name = "property")
    @JsonProperty("property")
    private String property;

    /**
     *
     */
    @Column(name = "unit")
    @JsonProperty("unit")
    private String unit;

    /**
     *
     */
    @Column(name = "env_fate_ind", length = 1)
    @JsonProperty("envFateInd")
    private String envFateInd;

    /**
     *
     */
    @Column(name = "exp_mean")
    @JsonProperty("experimentalAverage")
    private Double expMean;

    /**
     *
     */
    @Column(name = "exp_median")
    @JsonProperty("experimentalMedian")
    private Double expMedian;

    /**
     *
     */
    @Column(name = "exp_min")
    @JsonProperty("experimentalMinimum")
    private Double expMin;

    /**
     *
     */
    @Column(name = "exp_max")
    @JsonProperty("experimentalMaximum")
    private Double expMax;

    /**
     *
     */
    @Column(name = "pred_mean")
    @JsonProperty("predictedAverage")
    private Double predMean;

    /**
     *
     */
    @Column(name = "pred_median")
    @JsonProperty("predictedMedian")
    private Double predMedian;

    /**
     *
     */
    @Column(name = "pred_min")
    @JsonProperty("predictedMinimum")
    private Double predMin;

    /**
     *
     */
    @Column(name = "pred_max")
    @JsonProperty("predictedMaximum")
    private Double predMax;

}
