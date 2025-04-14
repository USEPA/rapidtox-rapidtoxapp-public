package gov.epa.ccte.api.rapidtox.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = {"gov.epa.ccte.api.rapidtox"}
)
public class PrimaryDataSourceConfiguration {

    @Primary
    @Bean(name = "primaryDataSourceProperties")
    @ConfigurationProperties("spring.main")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public PlatformTransactionManager primaryTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource(@Qualifier("primaryDataSourceProperties") DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder primaryEntityManagerFactoryBuilder, @Qualifier("primaryDataSource") DataSource primaryDataSource) {

        //Map<String, String> primaryJpaProperties = new HashMap<>();
        return primaryEntityManagerFactoryBuilder
                .dataSource(primaryDataSource)
                .packages("gov.epa.ccte.api.rapidtox")
                .persistenceUnit("primaryDataSource")
                .build();
    }
}
