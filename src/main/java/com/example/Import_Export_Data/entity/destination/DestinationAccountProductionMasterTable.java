package com.example.Import_Export_Data.entity.destination;

import com.example.Import_Export_Data.entity.source.AccountProductionMasterTable;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "account_production_master_tables", schema = "master")
public class DestinationAccountProductionMasterTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "master_section_id")
    private Integer masterSectionId;

    @Column(name = "sub_section_id")
    private Integer subSectionId;

    @Column(name = "master_chart_of_account_id")
    private Integer masterChartOfAccountId;

    @Column(name = "table_json", columnDefinition = "TEXT")
    private String tableJson;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "is_editable")
    private Boolean isEditable;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Time createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Time updatedDate;

    @Column(name = "ap_version")
    private Integer apVersion;

    @Column(name = "header_type")
    private Integer headerType;

    @Column(name = "checklist_question_reference")
    private String checklistQuestionReference;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "fsa_area_id")
    private Integer fsaAreaId;

    @Column(name = "original_table_id")
    private Integer originalTableId;

    @Column(name = "isckeditorcheck")
    private Boolean isCkeditorCheck;

    public DestinationAccountProductionMasterTable() {
    }

    public DestinationAccountProductionMasterTable(AccountProductionMasterTable source) {
        this.menuName = source.getMenuName();
        this.masterSectionId = source.getMasterSectionId();
        this.subSectionId = source.getSubSectionId();
        this.masterChartOfAccountId = source.getMasterChartOfAccountId();
        this.tableJson = source.getTableJson();
        this.referenceId = source.getReferenceId();
        this.isEditable = source.getEditable();
        this.isActive = source.getActive();
        this.isDeleted = source.getDeleted();
        this.createdBy = source.getCreatedBy();
        this.createdDate = source.getCreatedDate();
        this.updatedBy = source.getUpdatedBy();
        this.updatedDate = source.getUpdatedDate();
        this.apVersion = source.getApVersion();
        this.headerType = source.getHeaderType();
        this.checklistQuestionReference = source.getChecklistQuestionReference();
        this.sequenceNumber = source.getSequenceNumber();
        this.fsaAreaId = source.getFsaAreaId();
        this.originalTableId = source.getOriginalTableId();
        this.isCkeditorCheck = source.getCkeditorCheck();
    }


    public DestinationAccountProductionMasterTable(Integer id, String menuName, Integer masterSectionId, Integer masterChartOfAccountId, Integer subSectionId, String tableJson, Integer referenceId, Boolean isEditable, Boolean isActive, Boolean isDeleted, Integer createdBy, Time createdDate, Integer updatedBy, Time updatedDate, Integer apVersion, Integer headerType, String checklistQuestionReference, Integer sequenceNumber, Integer fsaAreaId, Integer originalTableId, Boolean isCkeditorCheck) {
        this.id = id;
        this.menuName = menuName;
        this.masterSectionId = masterSectionId;
        this.masterChartOfAccountId = masterChartOfAccountId;
        this.subSectionId = subSectionId;
        this.tableJson = tableJson;
        this.referenceId = referenceId;
        this.isEditable = isEditable;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.apVersion = apVersion;
        this.headerType = headerType;
        this.checklistQuestionReference = checklistQuestionReference;
        this.sequenceNumber = sequenceNumber;
        this.fsaAreaId = fsaAreaId;
        this.originalTableId = originalTableId;
        this.isCkeditorCheck = isCkeditorCheck;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMasterSectionId() {
        return masterSectionId;
    }

    public void setMasterSectionId(Integer masterSectionId) {
        this.masterSectionId = masterSectionId;
    }

    public Integer getSubSectionId() {
        return subSectionId;
    }

    public void setSubSectionId(Integer subSectionId) {
        this.subSectionId = subSectionId;
    }

    public Integer getMasterChartOfAccountId() {
        return masterChartOfAccountId;
    }

    public void setMasterChartOfAccountId(Integer masterChartOfAccountId) {
        this.masterChartOfAccountId = masterChartOfAccountId;
    }

    public String getTableJson() {
        return tableJson;
    }

    public void setTableJson(String tableJson) {
        this.tableJson = tableJson;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Time getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Time createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Time getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Time updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getApVersion() {
        return apVersion;
    }

    public void setApVersion(Integer apVersion) {
        this.apVersion = apVersion;
    }

    public Integer getHeaderType() {
        return headerType;
    }

    public void setHeaderType(Integer headerType) {
        this.headerType = headerType;
    }

    public String getChecklistQuestionReference() {
        return checklistQuestionReference;
    }

    public void setChecklistQuestionReference(String checklistQuestionReference) {
        this.checklistQuestionReference = checklistQuestionReference;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getFsaAreaId() {
        return fsaAreaId;
    }

    public void setFsaAreaId(Integer fsaAreaId) {
        this.fsaAreaId = fsaAreaId;
    }

    public Integer getOriginalTableId() {
        return originalTableId;
    }

    public void setOriginalTableId(Integer originalTableId) {
        this.originalTableId = originalTableId;
    }

    public Boolean getCkeditorCheck() {
        return isCkeditorCheck;
    }

    public void setCkeditorCheck(Boolean ckeditorCheck) {
        isCkeditorCheck = ckeditorCheck;
    }
}
