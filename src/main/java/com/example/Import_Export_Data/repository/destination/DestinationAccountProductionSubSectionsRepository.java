package com.example.Import_Export_Data.repository.destination;

import com.example.Import_Export_Data.entity.destination.DestinationAccountProductionSubSections;
import com.example.Import_Export_Data.entity.source.AccountProductionSubSections;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationAccountProductionSubSectionsRepository extends JpaRepository<DestinationAccountProductionSubSections, Integer> {
    List<DestinationAccountProductionSubSections> findByMasterChartOfAccountId(int masterChartOfAccountId);

    @Transactional
    @Modifying
    @Query("UPDATE DestinationAccountProductionSubSections d SET d.isDeleted = true WHERE d.masterChartOfAccountId = :masterChartOfAccountId")
    void softDeleteByMasterChartOfAccountId(@Param("masterChartOfAccountId") int masterChartOfAccountId);

}
