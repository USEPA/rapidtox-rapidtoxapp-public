
package gov.epa.ccte.api.rapidtox.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
@Deprecated
public class KeyCloakResponse {

    @JsonProperty("access")
    private Access access;

    @JsonProperty("createdTimestamp")
    private long createdTimestamp;

    @JsonProperty("disableableCredentialTypes")
    private List<Object> disableableCredentialTypes;

    @JsonProperty("email")
    private String email;

    @JsonProperty("emailVerified")
    private Boolean emailVerified;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("id")
    private String id;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("notBefore")
    private long notBefore;

    @JsonProperty("requiredActions")
    private List<Object> requiredActions;

    @JsonProperty("totp")
    private Boolean totp;

    @JsonProperty("username")
    private String username;

}
