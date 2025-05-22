package com.example.Import_Export_Data.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.Import_Export_Data.repository.destination",
        entityManagerFactoryRef = "destinationEntityManagerFactory",
        transactionManagerRef = "destinationTransactionManager"
)
public class DestinationDatabaseConfig {

    @Bean(name = "destinationDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.destination")
    public DataSource destinationDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "destinationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean destinationEntityManagerFactory(
            @Qualifier("destinationDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.Import_Export_Data.entity.destination");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPersistenceUnitName("destination-db");

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        factory.setJpaPropertyMap(properties);

        return factory;
    }

    @Bean(name = "destinationTransactionManager")
    public PlatformTransactionManager destinationTransactionManager(
            @Qualifier("destinationEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
