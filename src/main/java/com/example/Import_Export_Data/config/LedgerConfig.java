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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.Import_Export_Data.repository.ledgerRepo",  // ✅ change if your repo is elsewhere
        entityManagerFactoryRef = "ledgerEntityManagerFactory",
        transactionManagerRef = "ledgerTransactionManager"
)
public class LedgerConfig {

    @Bean(name = "ledgerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ledger")
    public DataSource ledgerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ledgerJdbcTemplate")
    public JdbcTemplate ledgerJdbcTemplate(@Qualifier("ledgerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "ledgerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ledgerEntityManagerFactory(
            @Qualifier("ledgerDataSource") DataSource ledgerDataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(ledgerDataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.example.Import_Export_Data.entity.ledgerEntity"); // ✅ path to ledger entities
        factory.setPersistenceUnitName("ledger");

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        factory.setJpaPropertyMap(jpaProperties);

        return factory;
    }

    @Bean(name = "ledgerTransactionManager")
    public PlatformTransactionManager ledgerTransactionManager(
            @Qualifier("ledgerEntityManagerFactory") EntityManagerFactory ledgerEntityManagerFactory) {
        return new JpaTransactionManager(ledgerEntityManagerFactory);
    }
}
