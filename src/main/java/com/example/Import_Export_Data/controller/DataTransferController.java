package com.example.Import_Export_Data.controller;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.service.DynamicDataTransferService;
import com.example.Import_Export_Data.service.TemporaryDatabaseStore;
import com.example.Import_Export_Data.service.TemporarySourceDatabaseStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class DataTransferController {
    private static final Logger logger = LoggerFactory.getLogger(DataTransferController.class);

    @Autowired
    private DynamicDataTransferService dynamicDataTransferService;

    @Autowired
    private TemporaryDatabaseStore temporaryDatabaseStore;

    @Autowired
    private TemporarySourceDatabaseStore temporarySourceDatabaseStore;

    @PostMapping("/dynamic-transfer/{id}")
    public ResponseEntity<String> transferToDynamicDb(@PathVariable Integer id) {
        logger.info("=== TRANSFER REQUEST STARTED ===");
        logger.info("Received dynamic transfer request for ID: {}", id);
        logger.info("Request timestamp: {}", System.currentTimeMillis());
        
        if (!temporaryDatabaseStore.isAvailable()) {
            logger.warn("Dynamic transfer failed: No database configuration found");
            return ResponseEntity.badRequest().body("No dynamic DB config found. Please configure it in Settings first.");
        }

        try {
            DestinationDbConfig config = temporaryDatabaseStore.get();
            logger.debug("Retrieved database configuration for transfer");
            
            dynamicDataTransferService.transferToDynamicDestination(id);
            logger.info("=== TRANSFER REQUEST COMPLETED SUCCESSFULLY ===");
            logger.info("Data transfer completed successfully for ID: {}", id);
            
            return ResponseEntity.ok("Data transferred to dynamic DB successfully!");
        } catch (Exception e) {
            logger.error("=== TRANSFER REQUEST FAILED ===");
            logger.error("Data transfer failed for ID: {} - Error: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Transfer failed: " + e.getMessage());
        }
    }

    @GetMapping("/is-source-configured")
    public ResponseEntity<Boolean> isSourceDatabaseConfigured() {
        boolean isConfigured = temporarySourceDatabaseStore.isAvailable();
        return ResponseEntity.ok(isConfigured);
    }
}
