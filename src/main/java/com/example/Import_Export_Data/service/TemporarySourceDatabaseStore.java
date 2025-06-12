package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.SourceDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TemporarySourceDatabaseStore {
    private static final Logger logger = LoggerFactory.getLogger(TemporarySourceDatabaseStore.class);
    private SourceDbConfig config;

    public void save(SourceDbConfig config) {
        logger.debug("Saving source database configuration for database: {}", config.getDbName());
        this.config = config;
    }

    public SourceDbConfig get() {
        logger.debug("Retrieving source database configuration");
        return config;
    }

    public boolean isAvailable() {
        boolean available = config != null;
        logger.debug("Source database configuration availability check: {}", available);
        return available;
    }

    public void clear() {
        logger.debug("Clearing source database configuration");
        config = null;
    }
} 