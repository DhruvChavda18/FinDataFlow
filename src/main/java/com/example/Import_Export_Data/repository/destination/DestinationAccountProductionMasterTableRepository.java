package com.example.Import_Export_Data.repository.destination;
import com.example.Import_Export_Data.entity.destination.DestinationAccountProductionMasterTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationAccountProductionMasterTableRepository extends JpaRepository<DestinationAccountProductionMasterTable, Integer> {
    List<DestinationAccountProductionMasterTable> findAllByMasterChartOfAccountId(int masterChartOfAccountId);

    @Modifying
    @Query("UPDATE DestinationAccountProductionMasterTable d SET d.isDeleted = true WHERE d.masterChartOfAccountId = :masterChartOfAccountId")
    void softDeleteByMasterChartOfAccountId(@Param("masterChartOfAccountId") int masterChartOfAccountId);
}
