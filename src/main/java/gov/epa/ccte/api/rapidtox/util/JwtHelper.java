package gov.epa.ccte.api.rapidtox.util;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.Assert;

public class JwtHelper {

    public static String emailFor(JwtAuthenticationToken token) {
        Assert.notNull(token, "token cannot be null");
        
        return (String) token.getTokenAttributes().get("email");
    }
}
