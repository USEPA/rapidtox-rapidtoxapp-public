package gov.epa.ccte.api.rapidtox.config;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    @Profile("!test")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("** loading security configuration");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests((authz)
                        -> authz
                        .requestMatchers(new AntPathRequestMatcher("/**/test")).permitAll()
                        .requestMatchers("/session-recall/**", "/recent-search/**", "/report/**", "/landscape/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2
                        -> oauth2.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info("** custom CORS configuration loaded");
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("** loading basic security configuration");
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.disable());  // Disable OAuth Resource Server

        // Add custom JWT filter
        http.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
