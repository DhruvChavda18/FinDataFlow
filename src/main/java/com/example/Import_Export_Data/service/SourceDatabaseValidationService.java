package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.SourceDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class SourceDatabaseValidationService {
    private static final Logger logger = LoggerFactory.getLogger(SourceDatabaseValidationService.class);
    
    private static final String[] REQUIRED_TABLES = {
        "master_chart_of_account",
        "account_production_master_sections",
        "account_production_sub_sections",
        "account_production_master_tables"
    };

    public ValidationResult validateSourceDatabase(SourceDbConfig config) {
        logger.info("Starting source database validation for database: {}", config.getDbName());
        List<String> missingTables = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(
                String.format("jdbc:postgresql://%s:%s/%s", 
                    config.getHost(), 
                    config.getPort(), 
                    config.getDbName()),
                config.getUsername(),
                config.getPassword())) {
            
            logger.debug("Successfully connected to source database");
            DatabaseMetaData metaData = conn.getMetaData();
            
            for (String tableName : REQUIRED_TABLES) {
                logger.debug("Checking for required table: {}", tableName);
                ResultSet tables = metaData.getTables(null, "master", tableName, new String[]{"TABLE"});
                if (!tables.next()) {
                    logger.warn("Required table not found: {}", tableName);
                    missingTables.add(tableName);
                }
            }
            
            if (missingTables.isEmpty()) {
                logger.info("Source database validation successful - all required tables present");
                return new ValidationResult(true, "Source database validation successful");
            } else {
                String message = "Missing required tables: " + String.join(", ", missingTables);
                logger.error("Source database validation failed: {}", message);
                return new ValidationResult(false, message);
            }
            
        } catch (Exception e) {
            String errorMessage = "Failed to validate source database: " + e.getMessage();
            logger.error(errorMessage, e);
            return new ValidationResult(false, errorMessage);
        }
    }

    public static class ValidationResult {
        private final boolean success;
        private final String message;

        public ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
} 