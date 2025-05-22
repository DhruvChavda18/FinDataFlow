package com.example.Import_Export_Data.repository.source;
import com.example.Import_Export_Data.entity.source.AccountProductionMasterTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceAccountProductionMasterTableRepository extends JpaRepository<AccountProductionMasterTable, Integer> {
    // Fetch all master tables by master_chart_of_account_id from source
    List<AccountProductionMasterTable> findAllByMasterChartOfAccountId(int masterChartOfAccountId);
}
