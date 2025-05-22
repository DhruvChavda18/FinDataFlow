package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.entity.destination.*;
import com.example.Import_Export_Data.entity.source.*;
import com.example.Import_Export_Data.repository.destination.*;
import com.example.Import_Export_Data.repository.source.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataTransferService {
    private static final Logger logger = LoggerFactory.getLogger(DataTransferService.class);

    @Autowired
    private SourceMasterChartOfAccountRepository sourceMasterChartOfAccountRepository;
    @Autowired
    private DestinationMasterChartOfAccountRepository destinationMasterChartOfAccountRepository;
    @Autowired
    private SourceAccountProductionMasterSectionRepository sourceSectionsRepository;
    @Autowired
    private DestinationAccountProductionMasterSectionRepository destinationSectionsRepository;
    @Autowired
    private SourceAccountProductionSubSectionsRepository sourceSubSectionRepository;
    @Autowired
    private DestinationAccountProductionSubSectionsRepository destinationSubSectionRepository;
    @Autowired
    private SourceAccountProductionMasterTableRepository sourceMasterTableRepository;
    @Autowired
    private DestinationAccountProductionMasterTableRepository destinationMasterTableRepository;

    @Transactional
    public void transferById(int sourceId) {
        logger.info("Starting data transfer for source ID: {}", sourceId);
        
        MasterChartOfAccount sourceAccount = sourceMasterChartOfAccountRepository.findById(sourceId)
                .orElseThrow(() -> {
                    logger.error("Source account not found with ID: {}", sourceId);
                    return new IllegalArgumentException("Source account with ID " + sourceId + " not found.");
                });
        logger.debug("Found source account: {}", sourceAccount.getChartofaccountname());

        // Soft Delete Existing Destination Records (if any)
        List<DestinationMasterChartOfAccount> existingAccounts =
                destinationMasterChartOfAccountRepository.findByChartOfAccountName(sourceAccount.getChartofaccountname());

        if (!existingAccounts.isEmpty()) {
            logger.info("Found {} existing accounts to soft delete", existingAccounts.size());
            for (DestinationMasterChartOfAccount oldAccount : existingAccounts) {
                logger.debug("Soft deleting account ID: {}", oldAccount.getId());
                // Soft delete Master Chart of Account
                destinationMasterChartOfAccountRepository.softDeleteByMasterChartOfAccountId(oldAccount.getId());

                // Soft delete related records
                destinationSectionsRepository.softDeleteByApVersion(oldAccount.getId());
                destinationSubSectionRepository.softDeleteByMasterChartOfAccountId(oldAccount.getId());
                destinationMasterTableRepository.softDeleteByMasterChartOfAccountId(oldAccount.getId());
            }
        }

        // Insert new Master Chart of Account
        logger.debug("Creating new destination account");
        DestinationMasterChartOfAccount newDestinationAccount = new DestinationMasterChartOfAccount();
        newDestinationAccount.setChartOfAccountName(sourceAccount.getChartofaccountname());
        newDestinationAccount.setDescription(sourceAccount.getDescription());
        newDestinationAccount.setP_version(sourceAccount.getP_version());
        newDestinationAccount.setPrev_chart_of_account_id(sourceAccount.getPrev_chart_of_account_id());
        newDestinationAccount.setIndustry_id(sourceAccount.getIndustry_id());
        newDestinationAccount.setDeleted(sourceAccount.getIs_deleted());
        newDestinationAccount.setIs_default(sourceAccount.getIs_default());
        newDestinationAccount.setCreated_by(sourceAccount.getCreated_by());
        newDestinationAccount.setCreated_date(sourceAccount.getCreated_date());
        newDestinationAccount.setUpdated_by(sourceAccount.getUpdated_by());
        newDestinationAccount.setUpdated_date(sourceAccount.getUpdated_date());
        newDestinationAccount.setIs_active(sourceAccount.getIs_active());
        newDestinationAccount.setStatus_id(sourceAccount.getStatus_id());
        newDestinationAccount.setIs_versioning(sourceAccount.getIs_versioning());
        newDestinationAccount.setIs_copy_chartofaccount(sourceAccount.getIs_copy_chartofaccount());
        newDestinationAccount.setCountryId(sourceAccount.getCountryId());
        newDestinationAccount.setSubstantialChange(sourceAccount.getSubstantialChange());

        newDestinationAccount = destinationMasterChartOfAccountRepository.saveAndFlush(newDestinationAccount);
        final int destinationAccountId = newDestinationAccount.getId();
        logger.info("Created new destination account with ID: {}", destinationAccountId);

        // Soft Delete Existing Sections in Destination
        logger.debug("Processing sections");
        List<DestinationAccountProductionMasterSection> existingSections = destinationSectionsRepository.findAllByApVersion(destinationAccountId);
        for (DestinationAccountProductionMasterSection oldSection : existingSections) {
            if (!oldSection.getDeleted()) {
                oldSection.setDeleted(true);
            }
        }
        destinationSectionsRepository.saveAll(existingSections);

        // Transfer Sections
        List<AccountProductionMasterSection> sourceSections = sourceSectionsRepository.findAllByApVersion(sourceId);
        logger.debug("Found {} sections to transfer", sourceSections.size());
        
        List<DestinationAccountProductionMasterSection> destinationSections = sourceSections.stream().map(section -> {
            DestinationAccountProductionMasterSection destinationSection = new DestinationAccountProductionMasterSection();
            destinationSection.setApVersion(destinationAccountId);
            destinationSection.setMenuName(section.getMenuName());
            destinationSection.setDeleted(section.getDeleted());
            destinationSection.setParentId(section.getParentId());
            destinationSection.setSerialNumber(section.getSerialNumber());
            destinationSection.setMenuType(section.getMenuType());
            destinationSection.setEditable(section.getEditable());
            destinationSection.setActive(section.getActive());
            destinationSection.setCreatedBy(section.getCreatedBy());
            destinationSection.setCreatedDate(section.getCreatedDate());
            destinationSection.setUpdatedBy(section.getUpdatedBy());
            destinationSection.setUpdatedDate(section.getUpdatedDate());
            destinationSection.setAliasName(section.getAliasName());
            destinationSection.setOriginalSectionId(section.getOriginalSectionId());
            return destinationSection;
        }).collect(Collectors.toList());

        destinationSectionsRepository.saveAll(destinationSections);
        logger.info("Transferred {} sections successfully", destinationSections.size());

        // Process Sub-sections
        logger.debug("Processing sub-sections");
        List<DestinationAccountProductionSubSections> existingSubSections =
                destinationSubSectionRepository.findByMasterChartOfAccountId(destinationAccountId);

        for (DestinationAccountProductionSubSections oldSubSection : existingSubSections) {
            if (!oldSubSection.getDeleted()) {
                oldSubSection.setDeleted(true);
            }
        }
        destinationSubSectionRepository.saveAll(existingSubSections);

        List<AccountProductionSubSections> sourceSubSections =
                sourceSubSectionRepository.findByMasterChartOfAccountId(sourceId);
        logger.debug("Found {} sub-sections to transfer", sourceSubSections.size());

        List<DestinationAccountProductionSubSections> destinationSubSections = sourceSubSections.stream().map(subSection -> {
            DestinationAccountProductionSubSections destinationSubSection = new DestinationAccountProductionSubSections();
            destinationSubSection.setMasterChartOfAccountId(destinationAccountId);
            destinationSubSection.setMasterSectionId(subSection.getMasterSectionId());
            destinationSubSection.setMenuName(subSection.getMenuName());
            destinationSubSection.setSerialNumber(subSection.getSerialNumber());
            destinationSubSection.setMenuType(subSection.getMenuType());
            destinationSubSection.setEditable(subSection.getEditable());
            destinationSubSection.setActive(subSection.getActive());
            destinationSubSection.setDeleted(subSection.getDeleted());
            destinationSubSection.setCreatedBy(subSection.getCreatedBy());
            destinationSubSection.setCreatedDate(subSection.getCreatedDate());
            destinationSubSection.setUpdatedBy(subSection.getUpdatedBy());
            destinationSubSection.setUpdatedDate(subSection.getUpdatedDate());
            destinationSubSection.setApVersion(subSection.getApVersion());
            destinationSubSection.setAliasName(subSection.getAliasName());
            destinationSubSection.setLoader(subSection.getLoader());
            destinationSubSection.setSectionSequence(subSection.getSectionSequence());
            destinationSubSection.setCashFlowType(subSection.getCashFlowType());
            destinationSubSection.setOriginalSubSectionId(subSection.getOriginalSubSectionId());
            return destinationSubSection;
        }).collect(Collectors.toList());

        destinationSubSectionRepository.saveAll(destinationSubSections);
        logger.info("Transferred {} sub-sections successfully", destinationSubSections.size());

        // Process Master Tables
        logger.debug("Processing master tables");
        List<DestinationAccountProductionMasterTable> existingMasterTables =
                destinationMasterTableRepository.findAllByMasterChartOfAccountId(destinationAccountId);

        for (DestinationAccountProductionMasterTable oldTable : existingMasterTables) {
            if (!oldTable.getDeleted()) {
                oldTable.setDeleted(true);
            }
        }
        destinationMasterTableRepository.saveAll(existingMasterTables);

        List<AccountProductionMasterTable> sourceMasterTables =
                sourceMasterTableRepository.findAllByMasterChartOfAccountId(sourceId);
        logger.debug("Found {} master tables to transfer", sourceMasterTables.size());

        List<DestinationAccountProductionMasterTable> destinationMasterTables = sourceMasterTables.stream().map(table -> {
            DestinationAccountProductionMasterTable destinationTable = new DestinationAccountProductionMasterTable();
            destinationTable.setMasterChartOfAccountId(destinationAccountId);
            destinationTable.setMasterSectionId(table.getMasterSectionId());
            destinationTable.setSubSectionId(table.getSubSectionId());
            destinationTable.setMenuName(table.getMenuName());
            destinationTable.setTableJson(table.getTableJson());
            destinationTable.setReferenceId(table.getReferenceId());
            destinationTable.setEditable(table.getEditable());
            destinationTable.setActive(table.getActive());
            destinationTable.setDeleted(table.getDeleted());
            destinationTable.setCreatedBy(table.getCreatedBy());
            destinationTable.setCreatedDate(table.getCreatedDate());
            destinationTable.setUpdatedBy(table.getUpdatedBy());
            destinationTable.setUpdatedDate(table.getUpdatedDate());
            destinationTable.setApVersion(table.getApVersion());
            destinationTable.setHeaderType(table.getHeaderType());
            destinationTable.setChecklistQuestionReference(table.getChecklistQuestionReference());
            destinationTable.setSequenceNumber(table.getSequenceNumber());
            destinationTable.setFsaAreaId(table.getFsaAreaId());
            destinationTable.setOriginalTableId(table.getOriginalTableId());
            destinationTable.setCkeditorCheck(table.getCkeditorCheck());
            return destinationTable;
        }).collect(Collectors.toList());

        destinationMasterTableRepository.saveAll(destinationMasterTables);
        logger.info("Transferred {} master tables successfully", destinationMasterTables.size());
        logger.info("Data transfer completed successfully for source ID: {}", sourceId);
    }
}
