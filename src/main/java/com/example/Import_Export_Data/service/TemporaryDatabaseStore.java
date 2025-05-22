package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TemporaryDatabaseStore {
    private static final Logger logger = LoggerFactory.getLogger(TemporaryDatabaseStore.class);
    private DestinationDbConfig config;

    public void save(DestinationDbConfig config) {
        logger.debug("Saving database configuration for database: {}", config.getDbName());
        this.config = config;
    }

    public DestinationDbConfig get() {
        logger.debug("Retrieving database configuration");
        return config;
    }

    public boolean isAvailable() {
        boolean available = config != null;
        logger.debug("Database configuration availability check: {}", available);
        return available;
    }

    public void clear() {
        logger.debug("Clearing database configuration");
        this.config = null;
    }
}

