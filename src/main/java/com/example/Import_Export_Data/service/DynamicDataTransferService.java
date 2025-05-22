package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.config.DynamicDatabaseConfig;
import com.example.Import_Export_Data.entity.destination.*;
import com.example.Import_Export_Data.entity.source.*;
import com.example.Import_Export_Data.repository.source.*;
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
    private SourceMasterChartOfAccountRepository sourceChartRepo;
    @Autowired
    private SourceAccountProductionMasterSectionRepository sourceSectionsRepo;
    @Autowired
    private SourceAccountProductionSubSectionsRepository sourceSubSectionsRepo;
    @Autowired
    private SourceAccountProductionMasterTableRepository sourceTablesRepo;

    @Autowired
    private DynamicDatabaseConfig dynamicDatabaseConfig;

    @Transactional
    public void transferToDynamicDestination(Integer sourceId, DestinationDbConfig config) throws Exception {
        logger.info("Starting dynamic data transfer for source ID: {} to database: {}", sourceId, config.getDbName());
        
        DataSource dynamicDataSource = dynamicDatabaseConfig.createDataSource(config);
        EntityManagerFactory emf = dynamicDatabaseConfig.createEntityManagerFactory(dynamicDataSource);
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            logger.debug("Testing database connection");
            em.createNativeQuery("SELECT 1").getSingleResult();
            logger.debug("Database connection successful");

            // 1. Fetch source MasterChart
            logger.debug("Fetching source MasterChart with ID: {}", sourceId);
            MasterChartOfAccount source = sourceChartRepo.findById(sourceId)
                    .orElseThrow(() -> {
                        logger.error("Source account not found with ID: {}", sourceId);
                        return new IllegalArgumentException("Invalid sourceId: " + sourceId);
                    });
            logger.debug("Found source account: {}", source.getChartofaccountname());

            // 2. Soft delete existing destination data by chart name
            logger.debug("Checking for existing accounts with name: {}", source.getChartofaccountname());
            List<DestinationMasterChartOfAccount> existingAccounts = em
                    .createQuery("SELECT d FROM DestinationMasterChartOfAccount d WHERE d.chartOfAccountName = :name", DestinationMasterChartOfAccount.class)
                    .setParameter("name", source.getChartofaccountname())
                    .getResultList();

            if (!existingAccounts.isEmpty()) {
                logger.info("Found {} existing accounts to soft delete", existingAccounts.size());
                for (DestinationMasterChartOfAccount oldAccount : existingAccounts) {
                    logger.debug("Soft deleting account ID: {}", oldAccount.getId());
                    oldAccount.setDeleted(true);
                    em.merge(oldAccount);

                    logger.debug("Soft deleting related sections for account ID: {}", oldAccount.getId());
                    em.createQuery("UPDATE DestinationAccountProductionMasterSection s SET s.isDeleted = true WHERE s.apVersion = :id")
                            .setParameter("id", oldAccount.getId())
                            .executeUpdate();

                    logger.debug("Soft deleting related sub-sections for account ID: {}", oldAccount.getId());
                    em.createQuery("UPDATE DestinationAccountProductionSubSections s SET s.isDeleted = true WHERE s.masterChartOfAccountId = :id")
                            .setParameter("id", oldAccount.getId())
                            .executeUpdate();

                    logger.debug("Soft deleting related tables for account ID: {}", oldAccount.getId());
                    em.createQuery("UPDATE DestinationAccountProductionMasterTable t SET t.isDeleted = true WHERE t.masterChartOfAccountId = :id")
                            .setParameter("id", oldAccount.getId())
                            .executeUpdate();
                }
            }

            // 3. Insert MasterChart
            logger.debug("Creating new destination account");
            DestinationMasterChartOfAccount destChart = new DestinationMasterChartOfAccount(source);
            em.persist(destChart);
            em.flush();
            int destId = destChart.getId();
            logger.info("Created new destination account with ID: {}", destId);

            // 4. Transfer Sections
            logger.debug("Fetching source sections");
            List<AccountProductionMasterSection> sourceSections = sourceSectionsRepo.findAllByApVersion(sourceId);
            logger.debug("Found {} sections to transfer", sourceSections.size());
            
            for (AccountProductionMasterSection s : sourceSections) {
                DestinationAccountProductionMasterSection d = new DestinationAccountProductionMasterSection(s);
                d.setApVersion(destId);
                em.persist(d);
            }
            logger.info("Transferred {} sections successfully", sourceSections.size());

            // 5. Transfer SubSections
            logger.debug("Fetching source sub-sections");
            List<AccountProductionSubSections> sourceSubSections = sourceSubSectionsRepo.findByMasterChartOfAccountId(sourceId);
            logger.debug("Found {} sub-sections to transfer", sourceSubSections.size());
            
            for (AccountProductionSubSections s : sourceSubSections) {
                DestinationAccountProductionSubSections d = new DestinationAccountProductionSubSections(s);
                d.setMasterChartOfAccountId(destId);
                em.persist(d);
            }
            logger.info("Transferred {} sub-sections successfully", sourceSubSections.size());

            // 6. Transfer Tables
            logger.debug("Fetching source tables");
            List<AccountProductionMasterTable> sourceTables = sourceTablesRepo.findAllByMasterChartOfAccountId(sourceId);
            logger.debug("Found {} tables to transfer", sourceTables.size());
            
            for (AccountProductionMasterTable t : sourceTables) {
                DestinationAccountProductionMasterTable d = new DestinationAccountProductionMasterTable(t);
                d.setMasterChartOfAccountId(destId);
                em.persist(d);
            }
            logger.info("Transferred {} tables successfully", sourceTables.size());

            em.getTransaction().commit();
            logger.info("Data transfer completed successfully for source ID: {} to database: {}", sourceId, config.getDbName());
        } catch (Exception e) {
            logger.error("Transfer failed for source ID: {} to database: {}", sourceId, config.getDbName(), e);
            em.getTransaction().rollback();
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        } finally {
            logger.debug("Closing EntityManager and EntityManagerFactory");
            em.close();
            emf.close();
        }
    }
}
