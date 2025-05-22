package com.example.Import_Export_Data.controller;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.service.DatabaseCreationService;
import com.example.Import_Export_Data.service.TemporaryDatabaseStore;
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

    @GetMapping("/is-configured")
    public ResponseEntity<Boolean> isDatabaseConfigured() {
        logger.debug("Checking if database is configured");
        boolean isConfigured = temporaryDatabaseStore.isAvailable();
        logger.info("Database configuration status: {}", isConfigured ? "configured" : "not configured");
        return ResponseEntity.ok(isConfigured);
    }

    @PostMapping("/reset-db")
    public ResponseEntity<String> resetDbConfig() {
        logger.info("Resetting database configuration");
        temporaryDatabaseStore.clear();
        logger.debug("Database configuration cleared from temporary store");
        return ResponseEntity.ok("Database configuration has been reset.");
    }
}
