package gov.epa.ccte.api.rapidtox.adme.model;

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
@Table(name = "stg_adme", schema = "rapidtox")
@Data
public class Adme {

    @Id
    @Column(name = "dtxsid", length = 45)
    private String dtxsid;

    /**
     *
     */
    @Column(name = "adme_fuhp")
    private Double admeFuhp;

    /**
     *
     */
    @Column(name = "adme_vol_of_dist")
    private Double admeVolOfDist;

    /**
     *
     */
    @Column(name = "adme_hsspc")
    private Double admeHsspc;

    /**
     *
     */
    @Column(name = "adme_pk_halflife")
    private Double admePkHalflife;

    /**
     *
     */
    @Column(name = "adme_invitro_hc")
    private Double admeInvitroHc;
}
