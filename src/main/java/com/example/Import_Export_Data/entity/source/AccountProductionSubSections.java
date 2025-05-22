package com.example.Import_Export_Data.entity.source;

import jakarta.persistence.*;

import java.sql.Time;


@Entity
@Table(name = "account_production_sub_sections", schema = "master")
public class AccountProductionSubSections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "master_section_id")
    private Integer masterSectionId;

    @Column(name = "master_chart_of_account_id")
    private Integer masterChartOfAccountId;

    @Column(name = "serialnumber")
    private Integer serialNumber;

    @Column(name = "menutype")
    private Integer menuType;

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

    @Column(name = "alias_name")
    private String aliasName;

    @Column(name = "is_loader")
    private Boolean isLoader;

    @Column(name = "section_sequence")
    private String sectionSequence;

    @Column(name = "cash_flow_type")
    private Integer cashFlowType;

    @Column(name = "original_sub_section_id")
    private Integer originalSubSectionId;


    public AccountProductionSubSections() {
    }

    public AccountProductionSubSections(Integer id, String menuName, Integer masterSectionId, Integer masterChartOfAccountId, Integer serialNumber, Integer menuType, Boolean isEditable, Boolean isActive, Boolean isDeleted, Integer createdBy, Time createdDate, Integer updatedBy, Time updatedDate, Integer apVersion, String aliasName, Boolean isLoader, String sectionSequence, Integer cashFlowType, Integer originalSubSectionId) {
        this.id = id;
        this.menuName = menuName;
        this.masterSectionId = masterSectionId;
        this.masterChartOfAccountId = masterChartOfAccountId;
        this.serialNumber = serialNumber;
        this.menuType = menuType;
        this.isEditable = isEditable;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.apVersion = apVersion;
        this.aliasName = aliasName;
        this.isLoader = isLoader;
        this.sectionSequence = sectionSequence;
        this.cashFlowType = cashFlowType;
        this.originalSubSectionId = originalSubSectionId;
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

    public Integer getMasterChartOfAccountId() {
        return masterChartOfAccountId;
    }

    public void setMasterChartOfAccountId(Integer masterChartOfAccountId) {
        this.masterChartOfAccountId = masterChartOfAccountId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Boolean getLoader() {
        return isLoader;
    }

    public void setLoader(Boolean loader) {
        isLoader = loader;
    }

    public String getSectionSequence() {
        return sectionSequence;
    }

    public void setSectionSequence(String sectionSequence) {
        this.sectionSequence = sectionSequence;
    }

    public Integer getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(Integer cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public Integer getOriginalSubSectionId() {
        return originalSubSectionId;
    }

    public void setOriginalSubSectionId(Integer originalSubSectionId) {
        this.originalSubSectionId = originalSubSectionId;
    }
}

