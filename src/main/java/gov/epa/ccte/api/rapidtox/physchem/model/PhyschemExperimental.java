package gov.epa.ccte.api.rapidtox.physchem.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author asif
 * Create at 2021-06-01 10:19
 */
@Entity
@Table(name = "physchem_experimental", schema = "rapidtox")
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PhyschemExperimental {

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
    @Column(name = "experimental_details")
    private String experimentalDetails;

    /**
     *
     */
    @Column(name = "env_fate_ind")
    private String envFateInd;

}
