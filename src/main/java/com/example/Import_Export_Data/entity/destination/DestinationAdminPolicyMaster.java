package com.example.Import_Export_Data.entity.destination;

import com.example.Import_Export_Data.entity.source.AdminPolicyMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_policy_master", schema = "master")
public class DestinationAdminPolicyMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chart_of_account_id")
    private Integer chartOfAccountId;

    @Column(name = "policy_id")
    private Integer policyId;

    @Column(name = "child_policy_id")
    private Integer childPolicyId;

    @Column(name = "sub_child_policy_id")
    private Integer subChildPolicyId;

    @Column(name = "header_name")
    private String headerName;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "serial_no")
    private Integer serialNo;

    @Column(name = "type")
    private Integer type;

    @Column(name = "ap_version")
    private Integer apVersion;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "display_sequence", length = 100)
    private String displaySequence;

    @Column(name = "original_policy_id")
    private Integer originalPolicyId;

    @Column(name = "filepath")
    private String filepath;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "filename")
    private String filename;

    @Column(name = "note_count")
    private String noteCount;

    @Column(name = "filetype")
    private String filetype;

    @Column(name = "is_updated")
    private Boolean isUpdated;

    @Column(name = "version")
    private Short version;

    public DestinationAdminPolicyMaster() {
    }

    public DestinationAdminPolicyMaster(AdminPolicyMaster source) {
        this.chartOfAccountId = source.getChartOfAccountId();
        this.policyId = source.getPolicyId();
        this.childPolicyId = source.getChildPolicyId();
        this.subChildPolicyId = source.getSubChildPolicyId();
        this.headerName = source.getHeaderName();
        this.description = source.getDescription();
        this.parentId = source.getParentId();
        this.serialNo = source.getSerialNo();
        this.type = source.getType();
        this.apVersion = source.getApVersion();
        this.isActive = source.getActive();
        this.isDeleted = source.getDeleted();
        this.createdBy = source.getCreatedBy();
        this.createdDate = source.getCreatedDate();
        this.updatedBy = source.getUpdatedBy();
        this.updatedDate = source.getUpdatedDate();
        this.displaySequence = source.getDisplaySequence();
        this.originalPolicyId = source.getOriginalPolicyId();
        this.filepath = source.getFilepath();
        this.documentId = source.getDocumentId();
        this.filename = source.getFilename();
        this.noteCount = source.getNoteCount();
        this.filetype = source.getFiletype();
        this.isUpdated = source.getUpdated();
        this.version = source.getVersion();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChartOfAccountId() {
        return chartOfAccountId;
    }

    public void setChartOfAccountId(Integer chartOfAccountId) {
        this.chartOfAccountId = chartOfAccountId;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public Integer getChildPolicyId() {
        return childPolicyId;
    }

    public void setChildPolicyId(Integer childPolicyId) {
        this.childPolicyId = childPolicyId;
    }

    public Integer getSubChildPolicyId() {
        return subChildPolicyId;
    }

    public void setSubChildPolicyId(Integer subChildPolicyId) {
        this.subChildPolicyId = subChildPolicyId;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getApVersion() {
        return apVersion;
    }

    public void setApVersion(Integer apVersion) {
        this.apVersion = apVersion;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(String displaySequence) {
        this.displaySequence = displaySequence;
    }

    public Integer getOriginalPolicyId() {
        return originalPolicyId;
    }

    public void setOriginalPolicyId(Integer originalPolicyId) {
        this.originalPolicyId = originalPolicyId;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(String noteCount) {
        this.noteCount = noteCount;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Boolean getUpdated() {
        return isUpdated;
    }

    public void setUpdated(Boolean updated) {
        isUpdated = updated;
    }

    public Short getVersion() {
        return version;
    }

    public void setVersion(Short version) {
        this.version = version;
    }
}
