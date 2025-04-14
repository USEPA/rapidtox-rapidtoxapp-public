package gov.epa.ccte.api.rapidtox.sessionreport.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "generated_reports", schema = "rapidtox_app_data")
@Data
@NoArgsConstructor
public class SessionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    @JsonProperty("reportId")
    private Integer reportId;

    @Column(name = "report_desc")
    @JsonProperty("reportDesc")
    private String reportDesc;

    @Column(name = "report_type")
    @JsonProperty("reportType")
    private String reportType;

    @Column(name = "session_key")
    @JsonProperty("sessionKey")
    private String sessionKey;

    @Column(name = "clowder_id")
    @JsonProperty("clowderId")
    private String clowderId;

    @Column(name = "created_by")
    @JsonProperty("createdBy")
    private String createdBy;

    @Column(name = "created_at")
    @UpdateTimestamp
    @JsonProperty("createdAt")
    private Instant createdAt;

}
