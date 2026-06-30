package com.example.Import_Export_Data.entity.source;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_production_ledger", schema = "master")
public class AccountProductionLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "master_chart_of_account_id")
    private Integer masterChartOfAccountId;

    @Column(name = "debit")
    private Double debit;

    @Column(name = "credit")
    private Double credit;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "version")
    private Integer version;

    @Column(name = "is_group")
    private Boolean isGroup;

    @Column(name = "is_ledger")
    private Boolean isLedger;

    @Column(name = "is_sub_group")
    private Boolean isSubGroup;

    @Column(name = "ledger_name", length = 256)
    private String ledgerName;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "ledger_type_id")
    private Short ledgerTypeId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "serialnumber")
    private Integer serialNumber;

    @Column(name = "ap_version")
    private Integer apVersion;

    @Column(name = "fsa_area_id")
    private Short fsaAreaId;

    @Column(name = "row_alias")
    private String rowAlias;

    @Column(name = "is_optional")
    private Boolean isOptional;

    @Column(name = "original_ledger_id")
    private Integer originalLedgerId;

    @Column(name = "srno")
    private Integer srno;

    public AccountProductionLedger() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMasterChartOfAccountId() {
        return masterChartOfAccountId;
    }

    public void setMasterChartOfAccountId(Integer masterChartOfAccountId) {
        this.masterChartOfAccountId = masterChartOfAccountId;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }

    public Boolean getLedger() {
        return isLedger;
    }

    public void setLedger(Boolean ledger) {
        isLedger = ledger;
    }

    public Boolean getSubGroup() {
        return isSubGroup;
    }

    public void setSubGroup(Boolean subGroup) {
        isSubGroup = subGroup;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Short getLedgerTypeId() {
        return ledgerTypeId;
    }

    public void setLedgerTypeId(Short ledgerTypeId) {
        this.ledgerTypeId = ledgerTypeId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getApVersion() {
        return apVersion;
    }

    public void setApVersion(Integer apVersion) {
        this.apVersion = apVersion;
    }

    public Short getFsaAreaId() {
        return fsaAreaId;
    }

    public void setFsaAreaId(Short fsaAreaId) {
        this.fsaAreaId = fsaAreaId;
    }

    public String getRowAlias() {
        return rowAlias;
    }

    public void setRowAlias(String rowAlias) {
        this.rowAlias = rowAlias;
    }

    public Boolean getOptional() {
        return isOptional;
    }

    public void setOptional(Boolean optional) {
        isOptional = optional;
    }

    public Integer getOriginalLedgerId() {
        return originalLedgerId;
    }

    public void setOriginalLedgerId(Integer originalLedgerId) {
        this.originalLedgerId = originalLedgerId;
    }

    public Integer getSrno() {
        return srno;
    }

    public void setSrno(Integer srno) {
        this.srno = srno;
    }
}
