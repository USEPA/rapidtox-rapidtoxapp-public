package gov.epa.ccte.api.rapidtox.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AnalogDataSourceConfiguration {

    @Bean(name = "similarityDataSource")
    @ConfigurationProperties(prefix = "spring.similarity")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "similarityTemplate")
    public NamedParameterJdbcTemplate jdbcTemplate1(@Qualifier("similarityDataSource") DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }
}
