package gov.epa.ccte.api.rapidtox.session.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PrevSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_key", unique = true, updatable = false)
    @JsonProperty("sessionKey")
    private String sessionKey;

    @Column(name = "session_title")
    private String sessionTitle;

    @Column(name = "update_timestamp")
    private Instant updateTimestamp;

    @Column(name = "workflow")
    private String workflow;
    
    @Column(name = "username")
    private String username;

    public PrevSession(String sessionKey, String sessionTitle, String username, Instant updateTimestamp, String workflow) {
        this.sessionKey = sessionKey;
        this.sessionTitle = sessionTitle;
        this.username = username;
        this.updateTimestamp = updateTimestamp;
        this.workflow = workflow;
    }

}
