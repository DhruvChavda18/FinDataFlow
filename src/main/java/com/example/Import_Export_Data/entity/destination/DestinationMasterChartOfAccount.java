package com.example.Import_Export_Data.entity.destination;


import com.example.Import_Export_Data.entity.source.MasterChartOfAccount;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "master_chart_of_account", schema = "master")
public class DestinationMasterChartOfAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  chartOfAccountName;
    private String description;
    private String p_version;
    private Integer prev_chart_of_account_id;
    private Integer industry_id;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    private Boolean is_default;
    private Time created_date;
    private Integer created_by;
    private Time updated_date;
    private Integer updated_by;
    private Boolean is_active;
    private Integer status_id;
    private Boolean is_versioning;
    private Boolean is_copy_chartofaccount;
    @Column(name = "country_id")
    private Integer countryId;
    @Column(name = "is_substantial_change")
    private Boolean isSubstantialChange;

    public DestinationMasterChartOfAccount() {
    }

    public DestinationMasterChartOfAccount(Integer id, String chartOfAccountName, String description, String p_version, Integer prev_chart_of_account_id, Integer industry_id, Boolean isDeleted, Boolean is_default, Time created_date, Integer created_by, Time updated_date, Integer updated_by, Boolean is_active, Integer status_id, Boolean is_versioning, Boolean is_copy_chartofaccount, Integer countryId, Boolean isSubstantialChange) {
        this.id = id;
        this.chartOfAccountName = chartOfAccountName;
        this.description = description;
        this.p_version = p_version;
        this.prev_chart_of_account_id = prev_chart_of_account_id;
        this.industry_id = industry_id;
        this.isDeleted = isDeleted;
        this.is_default = is_default;
        this.created_date = created_date;
        this.created_by = created_by;
        this.updated_date = updated_date;
        this.updated_by = updated_by;
        this.is_active = is_active;
        this.status_id = status_id;
        this.is_versioning = is_versioning;
        this.is_copy_chartofaccount = is_copy_chartofaccount;
        this.countryId = countryId;
        this.isSubstantialChange = isSubstantialChange;
    }

    public DestinationMasterChartOfAccount(MasterChartOfAccount source) {
        this.chartOfAccountName = source.getChartofaccountname();
        this.description = source.getDescription();
        this.p_version = source.getP_version();
        this.prev_chart_of_account_id = source.getPrev_chart_of_account_id();
        this.industry_id = source.getIndustry_id();
        this.isDeleted = source.getIs_deleted();
        this.is_default = source.getIs_default();
        this.created_date = source.getCreated_date();
        this.created_by = source.getCreated_by();
        this.updated_date = source.getUpdated_date();
        this.updated_by = source.getUpdated_by();
        this.is_active = source.getIs_active();
        this.status_id = source.getStatus_id();
        this.is_versioning = source.getIs_versioning();
        this.is_copy_chartofaccount = source.getIs_copy_chartofaccount();
        this.countryId = source.getCountryId();
        this.isSubstantialChange = source.getSubstantialChange();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChartOfAccountName() {
        return chartOfAccountName;
    }

    public void setChartOfAccountName(String chartOfAccountName) {
        this.chartOfAccountName = chartOfAccountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getP_version() {
        return p_version;
    }

    public void setP_version(String p_version) {
        this.p_version = p_version;
    }

    public Integer getPrev_chart_of_account_id() {
        return prev_chart_of_account_id;
    }

    public void setPrev_chart_of_account_id(Integer prev_chart_of_account_id) {
        this.prev_chart_of_account_id = prev_chart_of_account_id;
    }

    public Integer getIndustry_id() {
        return industry_id;
    }

    public void setIndustry_id(Integer industry_id) {
        this.industry_id = industry_id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public Time getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Time created_date) {
        this.created_date = created_date;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public Time getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Time updated_date) {
        this.updated_date = updated_date;
    }

    public Integer getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Integer updated_by) {
        this.updated_by = updated_by;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Boolean getIs_versioning() {
        return is_versioning;
    }

    public void setIs_versioning(Boolean is_versioning) {
        this.is_versioning = is_versioning;
    }

    public Boolean getIs_copy_chartofaccount() {
        return is_copy_chartofaccount;
    }

    public void setIs_copy_chartofaccount(Boolean is_copy_chartofaccount) {
        this.is_copy_chartofaccount = is_copy_chartofaccount;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Boolean getSubstantialChange() {
        return isSubstantialChange;
    }

    public void setSubstantialChange(Boolean substantialChange) {
        isSubstantialChange = substantialChange;
    }
}
