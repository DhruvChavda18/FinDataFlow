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
        
        try {
            // Step 1: Test connection to PostgreSQL server
            String systemDb = "postgres";
            String systemUrl = String.format("jdbc:postgresql://%s:%s/%s",
                config.getHost(),
                config.getPort(),
                systemDb);

            try (Connection conn = DriverManager.getConnection(systemUrl, config.getUsername(), config.getPassword())) {
                logger.debug("Successfully connected to PostgreSQL server");
                
                // Step 2: Check if database exists
                ResultSet rs = conn.getMetaData().getCatalogs();
                boolean dbExists = false;
                while (rs.next()) {
                    if (rs.getString(1).equalsIgnoreCase(config.getDbName())) {
                        dbExists = true;
                        break;
                    }
                }
                
                if (!dbExists) {
                    logger.warn("Database {} does not exist", config.getDbName());
                    return new ValidationResult(false, "Database does not exist.");
                }
                
                // Step 3: Connect to the specific database and check tables
                String dbUrl = String.format("jdbc:postgresql://%s:%s/%s",
                    config.getHost(),
                    config.getPort(),
                    config.getDbName());
                
                try (Connection dbConn = DriverManager.getConnection(dbUrl, config.getUsername(), config.getPassword())) {
                    logger.debug("Successfully connected to database: {}", config.getDbName());
                    
                    // Check for required tables
                    List<String> missingTables = new ArrayList<>();
                    DatabaseMetaData metaData = dbConn.getMetaData();
                    
                    for (String tableName : REQUIRED_TABLES) {
                        ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
                        if (!tables.next()) {
                            missingTables.add(tableName);
                        }
                    }
                    
                    if (!missingTables.isEmpty()) {
                        String message = "Database exists, but required tables are missing: " + String.join(", ", missingTables);
                        logger.warn(message);
                        return new ValidationResult(false, message);
                    }
                    
                    logger.info("Source database validation successful");
                    return new ValidationResult(true, "Source database configuration is successful.");
                }
            }
        } catch (Exception e) {
            logger.error("Source database validation failed: {}", e.getMessage(), e);
            return new ValidationResult(false, "Connection failed. Please check your credentials or IP/port.");
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