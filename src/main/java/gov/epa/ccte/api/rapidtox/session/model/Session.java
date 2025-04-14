package gov.epa.ccte.api.rapidtox.session.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "session_recall", schema = "rapidtox_app_data")
@Data
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_key", unique = true, updatable = false)
    @JsonProperty("sessionKey")
    private String sessionKey;

    @NotNull
    @Column(name = "session_title")

    @JsonProperty("sessionTitle")
    private String sessionTitle;

    @NotNull
    @Column(name = "username")
    @JsonProperty("username")
    private String username;

    @JsonProperty("createTimestamp")
    @Column(name = "create_timestamp")
    private Instant createTimestamp;

    @JsonProperty("updateTimestamp")
    @Column(name = "update_timestamp")
    private Instant updateTimestamp;

    @Column(name = "active_tab")
    private String activeTab;

    @JsonProperty("chemicalSearch")
    @Column(name = "chemical_search")
    private String chemicalSearch;

    @JsonProperty("hazard")
    @Column(name = "hazard")
    private String hazard;

    @JsonProperty("physchem")
    @Column(name = "physchem")
    private String physchem;

    @JsonProperty("analogue")
    @Column(name = "analogue")
    private String analogue;

    @JsonProperty("bioactivity")
    @Column(name = "bioactivity")
    private String bioactivity;

    @JsonProperty("pod")
    @Column(name = "pod")
    private String pod;

    @JsonProperty("report")
    @Column(name = "report")
    private String report;

    @JsonProperty("envFate")
    @Column(name = "env_fate")
    private String envFate;

    @JsonProperty("originalUsername")
    @Column(name = "original_username")
    private String originalUsername;

    @JsonProperty("workflow")
    @Column(name = "workflow")
    private String workflow;

    @JsonProperty("originalSessionKey")
    @Column(name = "original_session_key")
    private String originalSessionKey;

    @PrePersist
    protected void onCreate() {
        createTimestamp = Instant.now();
        updateTimestamp = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = Instant.now();
    }

}
