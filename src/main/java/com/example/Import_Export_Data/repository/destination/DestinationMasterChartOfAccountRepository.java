package com.example.Import_Export_Data.repository.destination;

import com.example.Import_Export_Data.entity.destination.DestinationMasterChartOfAccount;
import com.example.Import_Export_Data.entity.source.MasterChartOfAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationMasterChartOfAccountRepository extends JpaRepository<DestinationMasterChartOfAccount, Integer> {
    // Check if a record exists based on chartOfAccountName
    boolean existsByChartOfAccountName(String chartOfAccountName);

    // Find records by chartOfAccountName
    List<DestinationMasterChartOfAccount> findByChartOfAccountName(String chartOfAccountName);

    @Transactional
    @Modifying
    @Query("UPDATE DestinationMasterChartOfAccount d SET d.isDeleted = true WHERE d.id = :masterChartOfAccountId")
    void softDeleteByMasterChartOfAccountId(@Param("masterChartOfAccountId") int masterChartOfAccountId);

}
