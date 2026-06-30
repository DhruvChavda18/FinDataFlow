package com.example.Import_Export_Data.entity.destination;

import com.example.Import_Export_Data.entity.source.AccountProductionInputSheet;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_production_input_sheet", schema = "master")
public class DestinationAccountProductionInputSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chart_of_account_id")
    private Integer chartOfAccountId;

    @Column(name = "description")
    private String description;

    @Column(name = "display_name")
    private String displayName;

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

    @Column(name = "version")
    private Integer version;

    @Column(name = "macter_code_id")
    private Integer macterCodeId;

    @Column(name = "parent_section_id")
    private Integer parentSectionId;

    public DestinationAccountProductionInputSheet() {
    }

    public DestinationAccountProductionInputSheet(AccountProductionInputSheet source) {
        this.chartOfAccountId = source.getChartOfAccountId();
        this.description = source.getDescription();
        this.displayName = source.getDisplayName();
        this.isActive = source.getActive();
        this.isDeleted = source.getDeleted();
        this.createdBy = source.getCreatedBy();
        this.createdDate = source.getCreatedDate();
        this.updatedBy = source.getUpdatedBy();
        this.updatedDate = source.getUpdatedDate();
        this.version = source.getVersion();
        this.macterCodeId = source.getMacterCodeId();
        this.parentSectionId = source.getParentSectionId();
    }

    public DestinationAccountProductionInputSheet(Integer id, Integer chartOfAccountId, String description, String displayName, Boolean isActive, Boolean isDeleted, Integer createdBy, LocalDateTime createdDate, Integer updatedBy, LocalDateTime updatedDate, Integer version, Integer macterCodeId, Integer parentSectionId) {
        this.id = id;
        this.chartOfAccountId = chartOfAccountId;
        this.description = description;
        this.displayName = displayName;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.version = version;
        this.macterCodeId = macterCodeId;
        this.parentSectionId = parentSectionId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getMacterCodeId() {
        return macterCodeId;
    }

    public void setMacterCodeId(Integer macterCodeId) {
        this.macterCodeId = macterCodeId;
    }

    public Integer getParentSectionId() {
        return parentSectionId;
    }

    public void setParentSectionId(Integer parentSectionId) {
        this.parentSectionId = parentSectionId;
    }
}
