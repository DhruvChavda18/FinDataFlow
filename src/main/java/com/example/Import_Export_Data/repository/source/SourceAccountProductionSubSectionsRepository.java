package com.example.Import_Export_Data.repository.source;

import com.example.Import_Export_Data.entity.source.AccountProductionSubSections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceAccountProductionSubSectionsRepository extends JpaRepository<AccountProductionSubSections, Integer> {
    // Find all sub-sections by master_chart_of_account_id and master_section_id if needed
    List<AccountProductionSubSections> findByMasterChartOfAccountId(Integer masterChartOfAccountId);
}
