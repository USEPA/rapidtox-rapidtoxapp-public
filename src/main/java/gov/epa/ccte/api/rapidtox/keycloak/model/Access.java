
package gov.epa.ccte.api.rapidtox.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("unused")
@Deprecated
public class Access {

    @JsonProperty("impersonate")
    private Boolean impersonate;

    @JsonProperty("manage")
    private Boolean manage;

    @JsonProperty("manageGroupMembership")
    private Boolean manageGroupMembership;

    @JsonProperty("mapRoles")
    private Boolean mapRoles;

    @JsonProperty("view")
    private Boolean view;

}
