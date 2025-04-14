package gov.epa.ccte.api.rapidtox.landscapereport.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "landscape_reports", schema = "rapidtox_app_data")
@Data
@NoArgsConstructor
public class LandscapeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    @JsonProperty("reportId")
    private Integer reportId;

    @Column(name = "dtxsid")
    @JsonProperty("dtxsid")
    private String dtxsid;

    @Column(name = "clowder_id")
    @JsonProperty("clowderId")
    private String clowderId;

    @Column(name = "session_key")
    @JsonProperty("sessionKey")
    private String sessionKey;

    @Column(name = "is_generated")
    @JsonProperty("isGenerated")
    private boolean isGenerated;

    @Column(name = "created_by")
    @JsonProperty("createdBy")
    private String createdBy;

    @Column(name = "generate")
    @JsonProperty("generate")
    private boolean generate;

    @Column(name = "version")
    @JsonProperty("version")
    private Integer version;
	
	@Column(name="workflow")
	@JsonProperty("workflow")
	private String workflow;

}
