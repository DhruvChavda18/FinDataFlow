package com.example.Import_Export_Data.repository.destination;

import com.example.Import_Export_Data.entity.destination.DestinationAccountProductionMasterSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationAccountProductionMasterSectionRepository extends JpaRepository<DestinationAccountProductionMasterSection, Integer> {
    Logger logger = LoggerFactory.getLogger(DestinationAccountProductionMasterSectionRepository.class);

    List<DestinationAccountProductionMasterSection> findAllByApVersion(int apVersion);

    @Modifying
    @Query("UPDATE DestinationAccountProductionMasterSection d SET d.isDeleted = true WHERE d.apVersion = :apVersion")
    void softDeleteByApVersion(@Param("apVersion") int apVersion);

}

