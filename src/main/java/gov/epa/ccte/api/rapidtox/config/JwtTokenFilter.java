package gov.epa.ccte.api.rapidtox.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {


    private static final String NONE_HEADER = Base64.getUrlEncoder().encodeToString("{\"alg\": \"none\"}".getBytes());
    private final JwtParser parser = Jwts.parser().unsecured().build();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = String.valueOf(request.getHeader("Authorization"));

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            
            String token = extractToken(authHeader);
            
            Claims claims = parseClaims(token);

            JwtAuthenticationToken authentication = buildAuthentication(claims, token);

            // Set authentication in the context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Claims parseClaims(String token) throws IllegalArgumentException, JwtException {
        log.debug("token-to-parse: {}", token);
        var parsedJwt = parser.parseUnsecuredClaims(token);
        // Parse token claims
        return parsedJwt.getPayload();
    }

    private String extractToken(String authHeader) {
        
        // isolate the payload from header / signature
        int payloadStart = authHeader.indexOf(".");
        int signatureStart = authHeader.lastIndexOf(".");
        String payload = authHeader.substring(payloadStart + 1, signatureStart);
        
        // add a no signature algorithm header with no signature
        return NONE_HEADER + "." + payload + ".";
    }

    private JwtAuthenticationToken buildAuthentication(Claims claims, String token) {
        // Extract email and create authentication token
        String email = claims.get("email", String.class);
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "RS256")
                .claim("email", email)
                .build();
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        authentication.setDetails(claims);
        return authentication;
    }

}
