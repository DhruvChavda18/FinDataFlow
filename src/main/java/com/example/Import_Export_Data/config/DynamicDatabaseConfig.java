package com.example.Import_Export_Data.config;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicDatabaseConfig {

    public DataSource createDataSource(DestinationDbConfig config) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/" + config.getDbName());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    public EntityManagerFactory createEntityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.Import_Export_Data.entity"); // your entity package
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Map<String, Object> jpaProps = new HashMap<>();
        jpaProps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProps.put("hibernate.hbm2ddl.auto", "update");

        factory.setJpaPropertyMap(jpaProps);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    public PlatformTransactionManager createTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

