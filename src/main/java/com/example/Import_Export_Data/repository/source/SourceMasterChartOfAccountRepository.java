package com.example.Import_Export_Data.repository.source;

import com.example.Import_Export_Data.entity.source.MasterChartOfAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceMasterChartOfAccountRepository extends JpaRepository<MasterChartOfAccount, Integer> {
    Logger logger = LoggerFactory.getLogger(SourceMasterChartOfAccountRepository.class);
}
