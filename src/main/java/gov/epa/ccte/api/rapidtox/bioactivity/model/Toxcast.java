package gov.epa.ccte.api.rapidtox.bioactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "bioactivity_model", schema = "rapidtox")
@Data
@NoArgsConstructor
public class Toxcast {
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @Column(name="dtxsid")
    private String dtxsid;

    @Column(name="chemical_name")
    private String chemicalName;

    @Column(name="model")
    private String model;
    @Column(name="receptor")
    private String receptor;
    @Column(name="agonist")
    private String agonist;
    @Column(name="antagonist")
    private String antagonist;
    @Column(name="binding")
    private String binding;
    @Column(name="help_tx")
    private String helpTx;
    @Column(name="create_timestamp")
    private Instant createTimestamp;
}
