package com.example.Import_Export_Data.entity.ledgerEntity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger", schema = "master")
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "version")
    private Integer version;

    @Column(name = "code", length = 16)
    private String code;

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

    @Column(name = "tb_menu_id")
    private Integer tbMenuId;

    @Column(name = "serialnumber")
    private Integer serialNumber;

    @Column(name = "formula")
    private String formula;

    @Column(name = "is_editable")
    private Boolean isEditable;

    @Column(name = "depreciation_ledger_id")
    private Integer depreciationLedgerId;

    @Column(name = "accumulated_depreciation_id")
    private Integer accumulatedDepreciationId;

    @Column(name = "is_optional")
    private Boolean isOptional;

    @Column(name = "ap_version")
    private Integer apVersion;

    @Column(name = "fsa_area_id")
    private Short fsaAreaId;

    @Column(name = "ledger_header", length = 100)
    private String ledgerHeader;

    @Transient
    private Integer groupId;

    @Transient
    private String groupName;

    @Transient
    private Integer subGroupId;

    @Transient
    private String subGroupName;

    public Ledger() {
    }

    public Ledger(Integer id, Timestamp createdDate, Boolean isActive, Boolean isDeleted, Timestamp updatedDate, Integer version, String code, Boolean isGroup, Boolean isLedger, Boolean isSubGroup, String ledgerName, Integer createdBy, Integer updatedBy, Short ledgerTypeId, Integer parentId, Integer tbMenuId, Integer serialNumber, String formula, Boolean isEditable, Integer depreciationLedgerId, Integer accumulatedDepreciationId, Boolean isOptional, Integer apVersion, Short fsaAreaId, String ledgerHeader) {
        this.id = id;
        this.createdDate = createdDate;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.updatedDate = updatedDate;
        this.version = version;
        this.code = code;
        this.isGroup = isGroup;
        this.isLedger = isLedger;
        this.isSubGroup = isSubGroup;
        this.ledgerName = ledgerName;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.ledgerTypeId = ledgerTypeId;
        this.parentId = parentId;
        this.tbMenuId = tbMenuId;
        this.serialNumber = serialNumber;
        this.formula = formula;
        this.isEditable = isEditable;
        this.depreciationLedgerId = depreciationLedgerId;
        this.accumulatedDepreciationId = accumulatedDepreciationId;
        this.isOptional = isOptional;
        this.apVersion = apVersion;
        this.fsaAreaId = fsaAreaId;
        this.ledgerHeader = ledgerHeader;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
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

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getTbMenuId() {
        return tbMenuId;
    }

    public void setTbMenuId(Integer tbMenuId) {
        this.tbMenuId = tbMenuId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }

    public Integer getDepreciationLedgerId() {
        return depreciationLedgerId;
    }

    public void setDepreciationLedgerId(Integer depreciationLedgerId) {
        this.depreciationLedgerId = depreciationLedgerId;
    }

    public Integer getAccumulatedDepreciationId() {
        return accumulatedDepreciationId;
    }

    public void setAccumulatedDepreciationId(Integer accumulatedDepreciationId) {
        this.accumulatedDepreciationId = accumulatedDepreciationId;
    }

    public Boolean getOptional() {
        return isOptional;
    }

    public void setOptional(Boolean optional) {
        isOptional = optional;
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

    public String getLedgerHeader() {
        return ledgerHeader;
    }

    public void setLedgerHeader(String ledgerHeader) {
        this.ledgerHeader = ledgerHeader;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(Integer subGroupId) {
        this.subGroupId = subGroupId;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }
}

