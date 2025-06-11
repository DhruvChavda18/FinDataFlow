package com.example.Import_Export_Data.controller;

import com.example.Import_Export_Data.DTO.FullLedgerInfoDTO;
import com.example.Import_Export_Data.service.LedgerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class LedgerController {
    private static final Logger logger = LoggerFactory.getLogger(LedgerController.class);
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/ledgers")
    public String getLedgerPage() {
        logger.info("Accessing ledger page");
        return "index";
    }

    @GetMapping("/ledger-content")
    public String getLedgerContent(Model model) {
        // UUID uuid = UUID.randomUUID();
        logger.info("Loading ledger content");
        model.addAttribute("ledgers", ledgerService.getAllLedger());
        model.addAttribute("apVersions", ledgerService.getDistinctApVersions());
        model.addAttribute("groups", ledgerService.getAllGroups());
        model.addAttribute("subgroups", ledgerService.getAllSubGroups());
        return "ledgerData";
    }

    @PostMapping("/api/ledger/update")
    @ResponseBody
    public ResponseEntity<?> updateLedger(@RequestBody Map<String, Object> request) {
        logger.info("Received ledger update request for id: {}", request.get("id"));
        logger.debug("Update request details: {}", request);
        
        try {
            if (request.get("id") == null) {
                logger.warn("Update request rejected: Missing id");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing id"));
            }
            boolean success = ledgerService.updateLedger(request);
            if (success) {
                logger.info("Ledger updated successfully for id: {}", request.get("id"));
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                logger.warn("Failed to update ledger for id: {}", request.get("id"));
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to update ledger"));
            }
        } catch (Exception e) {
            logger.error("Error updating ledger: ", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/api/ledger/create")
    @ResponseBody
    public ResponseEntity<?> createLedger(@RequestBody Map<String, Object> request) {
        logger.info("Received ledger creation request");
        logger.debug("Create request details: {}", request);
        
        try {
            FullLedgerInfoDTO newLedger = ledgerService.createLedger(request);
            logger.info("Ledger created successfully with id: {}", newLedger.getId());
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Ledger created successfully"));
        } catch (Exception e) {
            logger.error("Error creating ledger: ", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/api/ledger/info/{id}")
    @ResponseBody
    public ResponseEntity<?> getLedgerInfo(@PathVariable Integer id) {
        logger.info("Received request for ledger info for id: {}", id);
        try {
            FullLedgerInfoDTO ledgerInfo = ledgerService.getLedgerDetailsById(id);
            if (ledgerInfo != null) {
                logger.info("Ledger info found for id: {}", id);
                return ResponseEntity.ok().body(ledgerInfo);
            } else {
                logger.warn("Ledger info not found for id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching ledger info for id: {}", id, e);
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error fetching ledger info: " + e.getMessage()));
        }
    }
}
