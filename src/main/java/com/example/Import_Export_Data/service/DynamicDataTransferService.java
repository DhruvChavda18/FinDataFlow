package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.DTO.SourceDbConfig;
import com.example.Import_Export_Data.config.DynamicDatabaseConfig;
import com.example.Import_Export_Data.entity.destination.*;
import com.example.Import_Export_Data.entity.source.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class DynamicDataTransferService {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataTransferService.class);

    @Autowired
    private DynamicDatabaseConfig dynamicDatabaseConfig;

    @Autowired
    private TemporarySourceDatabaseStore temporarySourceDatabaseStore;

    @Autowired
    private TemporaryDatabaseStore temporaryDatabaseStore;

    @Transactional
    public void transferToDynamicDestination(Integer sourceId) throws Exception {
        logger.info("Starting dynamic data transfer for source ID: {}", sourceId);

        // Fetch configs from temporary stores
        SourceDbConfig sourceConfig = temporarySourceDatabaseStore.get();
        DestinationDbConfig destConfig = temporaryDatabaseStore.get();

        if (sourceConfig == null) {
            throw new IllegalStateException("Source database configuration not found.");
        }
        if (destConfig == null) {
            throw new IllegalStateException("Destination database configuration not found.");
        }

        // Create dynamic EntityManagers for source and destination
        DataSource sourceDataSource = dynamicDatabaseConfig.createDataSource(sourceConfig);
        EntityManagerFactory sourceEmf = dynamicDatabaseConfig.createEntityManagerFactory(sourceDataSource);
        EntityManager sourceEm = sourceEmf.createEntityManager();

        DataSource destDataSource = dynamicDatabaseConfig.createDataSource(destConfig);
        EntityManagerFactory destEmf = dynamicDatabaseConfig.createEntityManagerFactory(destDataSource);
        EntityManager destEm = destEmf.createEntityManager();

        destEm.getTransaction().begin();

        try {
            // 1. Fetch source MasterChart
            logger.debug("Fetching source MasterChart with ID: {}", sourceId);
            MasterChartOfAccount source = sourceEm.find(MasterChartOfAccount.class, sourceId);
            if (source == null) {
                logger.error("Source account not found with ID: {}", sourceId);
                throw new IllegalArgumentException("Invalid sourceId: " + sourceId);
            }
            logger.debug("Found source account: {}", source.getChartofaccountname());

            // 2. Soft delete existing destination data by chart name
            logger.debug("Checking for existing accounts with name: {}", source.getChartofaccountname());
            List<DestinationMasterChartOfAccount> existingAccounts = destEm
                    .createQuery("SELECT d FROM DestinationMasterChartOfAccount d WHERE d.chartOfAccountName = :name", DestinationMasterChartOfAccount.class)
                    .setParameter("name", source.getChartofaccountname())
                    .getResultList();

            if (!existingAccounts.isEmpty()) {
                logger.info("Found {} existing accounts to soft delete", existingAccounts.size());
                for (DestinationMasterChartOfAccount oldAccount : existingAccounts) {
                    logger.debug("Soft deleting account ID: {}", oldAccount.getId());
                    oldAccount.setDeleted(true);
                    destEm.merge(oldAccount);

                    logger.debug("Soft deleting related sections for account ID: {}", oldAccount.getId());
                    destEm.createQuery("UPDATE DestinationAccountProductionMasterSection s SET s.isDeleted = true WHERE s.apVersion = :id")
                            .setParameter("id", oldAccount.getId())
                            .executeUpdate();

                    logger.debug("Soft deleting related sub-sections for account ID: {}", oldAccount.getId());
                    destEm.createQuery("UPDATE DestinationAccountProductionSubSections s SET s.isDeleted = true WHERE s.masterChartOfAccountId = :id")
                            .setParameter("id", oldAccount.getId())
                            .executeUpdate();

                    logger.debug("Soft deleting related tables for account ID: {}", oldAccount.getId());
                    destEm.createQuery("UPDATE DestinationAccountProductionMasterTable t SET t.isDeleted = true WHERE t.masterChartOfAccountId = :id")
                            .setParameter("id", oldAccount.getId())
                            .executeUpdate();
                }
            }

            // 3. Insert MasterChart
            logger.debug("Creating new destination account");
            DestinationMasterChartOfAccount destChart = new DestinationMasterChartOfAccount(source);
            destEm.persist(destChart);
            destEm.flush();
            int destId = destChart.getId();
            logger.info("Created new destination account with ID: {}", destId);

            // 4. Transfer Sections
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

            // 5. Transfer SubSections
            logger.debug("Fetching source sub-sections");
            List<AccountProductionSubSections> sourceSubSections = sourceEm
                .createQuery("SELECT s FROM AccountProductionSubSections s WHERE s.masterChartOfAccountId = :id", AccountProductionSubSections.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} sub-sections to transfer", sourceSubSections.size());
            for (AccountProductionSubSections s : sourceSubSections) {
                DestinationAccountProductionSubSections d = new DestinationAccountProductionSubSections(s);
                d.setMasterChartOfAccountId(destId);
                destEm.persist(d);
            }
            logger.info("Transferred {} sub-sections successfully", sourceSubSections.size());

            // 6. Transfer Tables
            logger.debug("Fetching source tables");
            List<AccountProductionMasterTable> sourceTables = sourceEm
                .createQuery("SELECT t FROM AccountProductionMasterTable t WHERE t.masterChartOfAccountId = :id", AccountProductionMasterTable.class)
                .setParameter("id", sourceId)
                .getResultList();
            logger.debug("Found {} tables to transfer", sourceTables.size());
            for (AccountProductionMasterTable t : sourceTables) {
                DestinationAccountProductionMasterTable d = new DestinationAccountProductionMasterTable(t);
                d.setMasterChartOfAccountId(destId);
                destEm.persist(d);
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
}
