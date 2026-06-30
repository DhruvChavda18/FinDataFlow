package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.DTO.SourceDbConfig;
import com.example.Import_Export_Data.config.DynamicDatabaseConfig;
import com.example.Import_Export_Data.entity.destination.*;
import com.example.Import_Export_Data.entity.source.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicDataTransferService {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataTransferService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DynamicDatabaseConfig dynamicDatabaseConfig;

    @Autowired
    private TemporarySourceDatabaseStore temporarySourceDatabaseStore;

    @Autowired
    private TemporaryDatabaseStore temporaryDatabaseStore;

    public void transferToDynamicDestination(Integer sourceId) throws Exception {
        logger.info("Starting dynamic data transfer for source ID: {}", sourceId);

        // Fetch configs from temporary stores
        SourceDbConfig sourceConfig = temporarySourceDatabaseStore.get();
        DestinationDbConfig destConfig = temporaryDatabaseStore.get();

        if (sourceConfig == null) {
            logger.error("Source database configuration not found");
            throw new IllegalStateException("Source database configuration not found.");
        }
        if (destConfig == null) {
            logger.error("Destination database configuration not found");
            throw new IllegalStateException("Destination database configuration not found.");
        }

        logger.debug("Creating dynamic EntityManagers for source and destination databases");
        // Create dynamic EntityManagers for source and destination
        DataSource sourceDataSource = dynamicDatabaseConfig.createDataSource(sourceConfig);
        EntityManagerFactory sourceEmf = dynamicDatabaseConfig.createEntityManagerFactory(sourceDataSource);
        EntityManager sourceEm = sourceEmf.createEntityManager();
        logger.debug("Source EntityManager created successfully");

        DataSource destDataSource = dynamicDatabaseConfig.createDataSource(destConfig);
        EntityManagerFactory destEmf = dynamicDatabaseConfig.createEntityManagerFactory(destDataSource);
        EntityManager destEm = destEmf.createEntityManager();
        logger.debug("Destination EntityManager created successfully");

        try {
            logger.debug("Starting destination transaction");
            destEm.getTransaction().begin();

            // 1. Fetch source MasterChart
            logger.debug("Fetching source MasterChart with ID: {}", sourceId);
            MasterChartOfAccount source = sourceEm.find(MasterChartOfAccount.class, sourceId);
            if (source == null) {
                logger.error("Source account not found with ID: {}", sourceId);
                throw new IllegalArgumentException("Invalid sourceId: " + sourceId);
            }
            logger.debug("Found source account: {}", source.getChartofaccountname());

            // 2. Insert MasterChart
            logger.debug("Creating new destination account");
            DestinationMasterChartOfAccount destChart = new DestinationMasterChartOfAccount(source);
            
            // Check for name collision and resolve
            String uniqueName = resolveUniqueChartOfAccountName(destEm, destChart.getChartOfAccountName());
            if (!uniqueName.equals(destChart.getChartOfAccountName())) {
                logger.info("Chart of account name conflict detected. Renaming from '{}' to '{}'", destChart.getChartOfAccountName(), uniqueName);
                destChart.setChartOfAccountName(uniqueName);
            }

            destEm.persist(destChart);
            destEm.flush();
            int destId = destChart.getId();
            logger.info("Created new destination account with ID: {}", destId);

            // 3. Transfer Sections
            logger.debug("Fetching source sections");
            List<AccountProductionMasterSection> sourceSections = sourceEm
                .createQuery("SELECT s FROM AccountProductionMasterSection s WHERE s.apVersion = :id", AccountProductionMasterSection.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} sections to transfer", sourceSections.size());
            for (AccountProductionMasterSection s : sourceSections) {
                DestinationAccountProductionMasterSection d = new DestinationAccountProductionMasterSection(s);
                d.setApVersion(destId);
                destEm.persist(d);
            }
            logger.info("Transferred {} sections successfully", sourceSections.size());

            // 4. Transfer Input Sheets
            logger.debug("Fetching source input sheets");
            List<AccountProductionInputSheet> sourceInputSheets = sourceEm
                .createQuery("SELECT i FROM AccountProductionInputSheet i WHERE i.chartOfAccountId = :id", AccountProductionInputSheet.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} input sheets to transfer", sourceInputSheets.size());
            for (AccountProductionInputSheet i : sourceInputSheets) {
                DestinationAccountProductionInputSheet d = new DestinationAccountProductionInputSheet(i);
                d.setChartOfAccountId(destId);
                destEm.persist(d);
            }
            logger.info("Transferred {} input sheets successfully", sourceInputSheets.size());

            // 5. Transfer Admin Policies
            logger.debug("Fetching source admin policies");
            List<AdminPolicyMaster> sourceAdminPolicies = sourceEm
                .createQuery("SELECT p FROM AdminPolicyMaster p WHERE p.chartOfAccountId = :id", AdminPolicyMaster.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} admin policies to transfer", sourceAdminPolicies.size());
            for (AdminPolicyMaster p : sourceAdminPolicies) {
                DestinationAdminPolicyMaster d = new DestinationAdminPolicyMaster(p);
                d.setChartOfAccountId(destId);
                d.setApVersion(destId);
                destEm.persist(d);
            }
            logger.info("Transferred {} admin policies successfully", sourceAdminPolicies.size());

            // 6. Transfer Ledgers
            logger.debug("Fetching source ledgers");
            List<AccountProductionLedger> sourceLedgers = sourceEm
                .createQuery("SELECT l FROM AccountProductionLedger l WHERE l.masterChartOfAccountId = :id", AccountProductionLedger.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} ledgers to transfer", sourceLedgers.size());
            Map<Integer, Integer> ledgerIdMap = new HashMap<>();
            for (AccountProductionLedger l : sourceLedgers) {
                DestinationAccountProductionLedger d = new DestinationAccountProductionLedger(l);
                d.setMasterChartOfAccountId(destId);
                destEm.persist(d);
                destEm.flush();
                ledgerIdMap.put(l.getId(), d.getId());
            }

            // Flush all pending inserts and clear the persistence context to prevent Hibernate from dirty-checking ledgers
            destEm.flush();
            destEm.clear();

            // Run native updates to bypass Hibernate trigger checks for parent_id
            for (AccountProductionLedger l : sourceLedgers) {
                Integer destLedgerId = ledgerIdMap.get(l.getId());
                Integer destParentId = resolveDestinationLedgerParentId(l, ledgerIdMap);
                destEm.createNativeQuery("UPDATE master.account_production_ledger SET parent_id = :parentId WHERE id = :id")
                    .setParameter("parentId", destParentId)
                    .setParameter("id", destLedgerId)
                    .executeUpdate();
            }
            logger.info("Transferred {} ledgers successfully", sourceLedgers.size());

            // 7. Transfer SubSections
            logger.debug("Fetching source sub-sections");
            List<AccountProductionSubSections> sourceSubSections = sourceEm
                .createQuery("SELECT s FROM AccountProductionSubSections s WHERE s.masterChartOfAccountId = :id", AccountProductionSubSections.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} sub-sections to transfer", sourceSubSections.size());
            Map<Integer, Integer> subSectionIdMap = new HashMap<>();
            for (AccountProductionSubSections s : sourceSubSections) {
                DestinationAccountProductionSubSections d = new DestinationAccountProductionSubSections(s);
                d.setMasterChartOfAccountId(destId);
                destEm.persist(d);
                destEm.flush();
                subSectionIdMap.put(s.getId(), d.getId());
            }
            logger.info("Transferred {} sub-sections successfully", sourceSubSections.size());

            // 8. Transfer Tables
            logger.debug("Fetching source tables");
            List<AccountProductionMasterTable> sourceTables = sourceEm
                .createQuery("SELECT t FROM AccountProductionMasterTable t WHERE t.masterChartOfAccountId = :id", AccountProductionMasterTable.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} tables to transfer", sourceTables.size());
            Map<Integer, String> tableJsonUpdates = new HashMap<>();
            for (AccountProductionMasterTable t : sourceTables) {
                DestinationAccountProductionMasterTable d = new DestinationAccountProductionMasterTable(t);
                d.setMasterChartOfAccountId(destId);
                d.setSubSectionId(resolveDestinationSubSectionId(t, subSectionIdMap));
                destEm.persist(d);
                destEm.flush();
                String updatedJson = updateTableJsonId(d.getTableJson(), d.getId());
                tableJsonUpdates.put(d.getId(), updatedJson);
            }

            // Flush all pending inserts and clear the persistence context to prevent Hibernate from dirty-checking tables
            destEm.flush();
            destEm.clear();

            // Run native updates to bypass Hibernate trigger checks for table_json
            for (Map.Entry<Integer, String> entry : tableJsonUpdates.entrySet()) {
                destEm.createNativeQuery("UPDATE master.account_production_master_tables SET table_json = CAST(:tableJson AS json) WHERE id = :id")
                    .setParameter("tableJson", entry.getValue())
                    .setParameter("id", entry.getKey())
                    .executeUpdate();
            }
            logger.info("Transferred {} tables successfully", sourceTables.size());

            destEm.getTransaction().commit();
            logger.info("Data transfer completed successfully for source ID: {}", sourceId);
        } catch (Exception e) {
            logger.error("Transfer failed for source ID: {}", sourceId, e);
            destEm.getTransaction().rollback();
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        } finally {
            logger.debug("Closing EntityManagers and EntityManagerFactories");
            sourceEm.close();
            sourceEmf.close();
            destEm.close();
            destEmf.close();
        }
    }

    private String updateTableJsonId(String tableJson, Integer destinationTableId) throws Exception {
        if (tableJson == null || tableJson.isBlank() || destinationTableId == null) {
            return tableJson;
        }

        JsonNode root = objectMapper.readTree(tableJson);
        if (!root.isObject()) {
            throw new IllegalArgumentException("table_json must be a JSON object to update id");
        }

        ObjectNode objectNode = (ObjectNode) root;
        objectNode.put("id", destinationTableId);
        return objectMapper.writeValueAsString(objectNode);
    }

    private Integer resolveDestinationSubSectionId(
        AccountProductionMasterTable sourceTable,
        Map<Integer, Integer> subSectionIdMap
    ) {
        Integer sourceSubSectionId = sourceTable.getSubSectionId();
        if (sourceSubSectionId == null) {
            return null;
        }

        Integer destinationSubSectionId = subSectionIdMap.get(sourceSubSectionId);
        if (destinationSubSectionId == null) {
            throw new IllegalStateException(
                "Destination sub-section not found for source table ID "
                    + sourceTable.getId()
                    + " and source sub-section ID "
                    + sourceSubSectionId
            );
        }

        return destinationSubSectionId;
    }

    private Integer resolveDestinationLedgerParentId(
        AccountProductionLedger sourceLedger,
        Map<Integer, Integer> ledgerIdMap
    ) {
        Integer sourceParentId = sourceLedger.getParentId();
        if (sourceParentId == null) {
            return null;
        }
        if (sourceParentId == 0) {
            return 0;
        }

        Integer destinationParentId = ledgerIdMap.get(sourceParentId);
        if (destinationParentId == null) {
            return 0;
        }

        return destinationParentId;
    }

    String resolveUniqueChartOfAccountName(EntityManager destEm, String originalName) {
        if (originalName == null || originalName.trim().isEmpty()) {
            return originalName;
        }

        // 1. Check if originalName exists
        if (!chartOfAccountNameExists(destEm, originalName)) {
            return originalName;
        }

        // 2. Determine baseName by removing trailing _TP or _TP_v<version>
        String baseName = originalName;
        if (originalName.matches(".*_TP_v\\d+")) {
            baseName = originalName.replaceAll("_TP_v\\d+$", "");
        } else if (originalName.endsWith("_TP")) {
            baseName = originalName.substring(0, originalName.length() - 3);
        }

        // 3. Find unique name starting with baseName_TP, baseName_TP_v1, baseName_TP_v2, ...
        String candidate = baseName + "_TP";
        if (!chartOfAccountNameExists(destEm, candidate)) {
            return candidate;
        }

        int version = 1;
        while (true) {
            candidate = baseName + "_TP_v" + version;
            if (!chartOfAccountNameExists(destEm, candidate)) {
                return candidate;
            }
            version++;
        }
    }

    private boolean chartOfAccountNameExists(EntityManager destEm, String name) {
        Long count = destEm.createQuery(
            "SELECT COUNT(d) FROM DestinationMasterChartOfAccount d WHERE d.chartOfAccountName = :name", Long.class)
            .setParameter("name", name)
            .getSingleResult();
        return count != null && count > 0;
    }
}
