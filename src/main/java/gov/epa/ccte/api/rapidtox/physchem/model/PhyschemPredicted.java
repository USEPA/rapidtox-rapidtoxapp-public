package gov.epa.ccte.api.rapidtox.physchem.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author asif
 * Create at 2021-06-01 10:21
 */
@Entity
@Table(name = "physchem_predicted", schema = "rapidtox")
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PhyschemPredicted {

    /**
     *
     */
    @Id
    @Setter(AccessLevel.PROTECTED)
    @NonNull
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     *
     */
    @Column(name = "dtxsid", length = 45)
    private String dtxsid;

    /**
     *
     */
    @Column(name = "property")
    private String property;

    /**
     *
     */
    @Column(name = "source")
    private String source;

    /**
     *
     */
    @Column(name = "result")
    private Double result;

    /**
     *
     */
    @Column(name = "unit")
    private String unit;

    /**
     *
     */
    @Column(name = "calculation_details")
    private String calculationDetails;

    /**
     *
     */
    @Column(name = "env_fate_ind")
    private String envFateInd;

    /**
     *
     */
    @Column(name = "model_desc")
    private String modelDesc;
}
