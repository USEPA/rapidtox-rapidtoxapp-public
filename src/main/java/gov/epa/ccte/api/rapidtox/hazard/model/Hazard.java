package gov.epa.ccte.api.rapidtox.hazard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author arashid Create at 2021-05-05 09:12
 */
@Entity
@Table(name = "hazard", schema = "rapidtox")
@Data
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Hazard {

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
    private String dtxsid;

    /**
     *
     */
    @Column(name = "casrn")
    private String casrn;

    /**
     *
     */
    @Column(name = "preferred_name")
    private String preferredName;

    /**
     *
     */
    @Column(name = "mol_wgt")
    private Double molecularWeight;

    /**
     *
     */
    @Column(name = "toxval_type")
    private String toxvalType;

    /**
     *
     */
    @Column(name = "toxval_subtype")
    private String toxvalSubtype;

    /**
     *
     */
    @Column(name = "risk_assessment_class", length = 45)
    private String riskAssessmentClass;

    /**
     *
     */
    @Column(name = "toxval_numeric")
    private Double toxvalNumeric;

    /**
     *
     */
    @Column(name = "toxval_units", length = 45)
    private String toxvalUnits;

    /**
     *
     */
    @Column(name = "study_type")
    private String studyType;

    /**
     *
     */
    @Column(name = "study_duration")
    private Double studyDuration;

    /**
     *
     */
    @Column(name = "study_duration_units")
    private String studyDurationUnits;

    /**
     *
     */
    @Column(name = "exposure_route")
    private String exposureRoute;

    /**
     *
     */
    @Column(name = "exposure_method")
    private String exposureMethod;

    /**
     *
     */
    @Column(name = "species_common")
    private String speciesCommon;

    /**
     *
     */
    @Column(name = "source")
    private String source;

    /**
     *
     */
    @Column(name = "critical_eff_nt")
    @JsonProperty("criticalEffNt")
    private String effect;

    /**
     *
     */
    @Column(name = "toxval_typ_defn")
    private String toxvalTypDefn;

    /**
     *
     */
    @Column(name = "create_timestamp")
    private OffsetDateTime createTimestamp;

    /**
     *
     */
    @Column(name = "super_category")
    private String superCategory;

    /**
     *
     */
    @Column(name = "source_url")
    private String sourceUrl;

    /**
     *
     */
    @Column(name = "ssource_url")
    private String ssourceUrl;

    /**
     *
     */
    @Column(name = "clowder_id")
    private String clowderId;

    /**
     *
     */
    @Column(name = "super_source")
    @JsonIgnore
    private String superSource;

    /**
     *
     */
    @Column(name = "workflow")
    private String workflow;

}
