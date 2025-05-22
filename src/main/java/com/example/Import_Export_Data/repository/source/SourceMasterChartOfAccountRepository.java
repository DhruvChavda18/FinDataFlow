package com.example.Import_Export_Data.repository.source;

import com.example.Import_Export_Data.entity.source.MasterChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceMasterChartOfAccountRepository extends JpaRepository<MasterChartOfAccount, Integer> {
}
