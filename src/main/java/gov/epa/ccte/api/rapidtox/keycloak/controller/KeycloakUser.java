package gov.epa.ccte.api.rapidtox.keycloak.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Deprecated
public class KeycloakUser {

    private String username;

    public KeycloakUser(String username) {
        this.username = username;
    }
}
