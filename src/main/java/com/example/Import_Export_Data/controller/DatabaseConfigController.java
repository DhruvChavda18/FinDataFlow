package com.example.Import_Export_Data.controller;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.DTO.SourceDbConfig;
import com.example.Import_Export_Data.service.DatabaseCreationService;
import com.example.Import_Export_Data.service.SourceDatabaseValidationService;
import com.example.Import_Export_Data.service.TemporaryDatabaseStore;
import com.example.Import_Export_Data.service.TemporarySourceDatabaseStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class DatabaseConfigController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfigController.class);

    @Autowired
    private DatabaseCreationService databaseCreationService;

    @Autowired
    private TemporaryDatabaseStore temporaryDatabaseStore;

    @Autowired
    private TemporarySourceDatabaseStore temporarySourceDatabaseStore;

    @Autowired
    private SourceDatabaseValidationService sourceDatabaseValidationService;

    @PostMapping("/create-db")
    public ResponseEntity<String> createDatabase(@RequestBody DestinationDbConfig config) {
        logger.info("Received database creation request for database: {}", config.getDbName());
        logger.debug("Database configuration details: username={}", config.getUsername());
        
        try {
            boolean created = databaseCreationService.createDatabase(config);
            logger.debug("Database creation status: {}", created ? "created" : "already exists");

            // Always save the config — even if DB already exists
            temporaryDatabaseStore.save(config);
            logger.debug("Database configuration saved to temporary store");

            if (created) {
                logger.info("New database created and configuration saved successfully");
                return ResponseEntity.ok("✅ New database created and configuration saved successfully.");
            } else {
                logger.info("Database configuration saved with existing database");
                return ResponseEntity.ok("✅ Database configuration details saved successfully with existing DB name.");
            }
        } catch (Exception e) {
            logger.error("Error creating database: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error: " + e.getMessage());
        }
    }

    @PostMapping("/validate-source")
    public ResponseEntity<String> validateSourceDatabase(@RequestBody SourceDbConfig config) {
        logger.info("Received source database validation request for database: {}", config.getDbName());
        
        try {
            SourceDatabaseValidationService.ValidationResult result = 
                sourceDatabaseValidationService.validateSourceDatabase(config);
            
            if (result.isSuccess()) {
                // Save the configuration if validation is successful
                temporarySourceDatabaseStore.save(config);
                logger.info("Source database configuration saved successfully");
                return ResponseEntity.ok("✅ " + result.getMessage());
            } else {
                logger.warn("Source database validation failed: {}", result.getMessage());
                return ResponseEntity.badRequest().body("❌ " + result.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error validating source database: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error: " + e.getMessage());
        }
    }

    @GetMapping("/is-configured")
    public ResponseEntity<Boolean> isDatabaseConfigured() {
        logger.debug("Checking if database is configured");
        boolean isConfigured = temporaryDatabaseStore.isAvailable();
        logger.info("Database configuration status: {}", isConfigured ? "configured" : "not configured");
        return ResponseEntity.ok(isConfigured);
    }

    @PostMapping("/reset-db")
    public ResponseEntity<String> resetDbConfig() {
        logger.info("Resetting destination database configuration");
        temporaryDatabaseStore.clear();
        logger.debug("Destination database configuration cleared from temporary store");
        return ResponseEntity.ok("Destination database configuration has been reset.");
    }

    @PostMapping("/reset-source")
    public ResponseEntity<String> resetSourceDbConfig() {
        logger.info("Resetting source database configuration");
        temporarySourceDatabaseStore.clear();
        logger.debug("Source database configuration cleared from temporary store");
        return ResponseEntity.ok("Source database configuration has been reset.");
    }
}
