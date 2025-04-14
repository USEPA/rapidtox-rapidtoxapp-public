package gov.epa.ccte.api.rapidtox.physchem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "hazard_similarity_phychem_aggr_er", schema = "rapidtox")
@Data
public class SimilarityPhychemAggrER {

    @Id
    @Column(name = "dtxsid")
    @JsonProperty("dtxsid")
    private String dtxsid;

    @Column(name = "dtxcid")
    @JsonProperty("dtxcid")
    private String dtxcid;

    @Column(name = "adme_fuhp")
    @JsonProperty("admeFuhp")
    private String admeFuhp;

    @Column(name = "adme_vol_of_dist")
    @JsonProperty("admeVolOfDist")
    private Double admeVolOfDist;

    @Column(name = "adme_hsspc")
    @JsonProperty("admeHsspc")
    private Double admeHsspc;

    @Column(name = "adme_pk_halflife")
    @JsonProperty("admePkHalflife")
    private Double admePkHalflife;

    @Column(name = "adme_invitro_hc")
    @JsonProperty("admeInvitroHc")
    private Double admeInvitroHc;

    @Column(name = "phychem_logkow")
    @JsonProperty("phychemLogkow")
    private Double phychemLogkow;

    @Column(name = "phychem_logkoa")
    @JsonProperty("phychemLogkoa")
    private Double phychemLogkoa;

    @Column(name = "phychem_vp")
    @JsonProperty("phychemVp")
    private Double phychemVp;

    @Column(name = "phychem_water_solubility")
    @JsonProperty("phychemWaterSolubility")
    private Double phychemWaterSolubility;

    @Column(name = "preferred_name")
    @JsonProperty("preferredName")
    private String preferredName;

    @Column(name = "has_structure_image")
    @JsonProperty("hasStructureImage")
    private Integer hasStructureImage;

    @Transient
    private Boolean isTarget = false;
}
