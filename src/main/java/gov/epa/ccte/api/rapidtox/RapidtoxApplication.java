package gov.epa.ccte.api.rapidtox;

import gov.epa.ccte.api.rapidtox.config.ApplicationProperties;
import gov.epa.ccte.api.rapidtox.config.DefaultProfileUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@OpenAPIDefinition
@EnableConfigurationProperties({ApplicationProperties.class})
public class RapidtoxApplication {

	private final Environment env;

	public RapidtoxApplication(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RapidtoxApplication.class);

		DefaultProfileUtil.addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	private static void logApplicationStartup(Environment env) {
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		String serverPort = env.getProperty("server.port");
		String contextPath = env.getProperty("server.servlet.context-path");
		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}
		log.info("\n----------------------------------------------------------\n\t"
				+ "Application '{}' is running! Access URLs:\n\t"
				+ "Local: \t\t{}://localhost:{}{}\n\t"
				+ "External: \t{}://{}:{}{}\n\t"
				+ "Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				protocol,
				serverPort,
				contextPath,
				protocol,
				hostAddress,
				serverPort,
				contextPath,
				env.getActiveProfiles());
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
    
    @Bean
    public Random rng() {
        return new Random();
    }

}
