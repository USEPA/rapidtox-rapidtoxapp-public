package gov.epa.ccte.api.rapidtox.chemical.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Immutable;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "search_chemical", schema = "rapidtox")
public class ChemicalSearch {
    @Id
    Integer id;

    @Column(name = "dtxsid")
    @JsonProperty("dtxsid")
    private String dtxsid;

    // column for UI - it is not in table
    @Transient
    @JsonProperty("selected")
    private Boolean selected;

    @Column(name = "preferred_name")
    @JsonProperty("preferredName")
    private String preferredName;

    @Column(name = "search_name")
    @JsonProperty(value = "searchMatch")
    private String searchMatch;

    @Column(name = "search_group")
    @JsonProperty(value = "searchGroup")
    private String searchGroup;

    @Column(name = "search_value")
    @JsonProperty("searchWord")
    private String searchWord;

    @Column(name = "modified_value")
    @JsonProperty("modifiedValue")
    private String modifiedValue;

    @Column(name = "rank")
    @JsonProperty("rank")
    private Integer rank;

    @Column(name = "dtxcid")
    @JsonProperty("dtxcid")
    private String dtxcid;

    @Column(name = "has_structure_image")
    @JsonProperty("hasStructureImage")
    private Boolean hasStructureImage;

    @Column(name = "casrn")
    @JsonProperty("casrn")
    private String carsn;
}
