package com.example.Import_Export_Data.repository.source;

import com.example.Import_Export_Data.entity.source.AccountProductionMasterSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceAccountProductionMasterSectionRepository extends JpaRepository<AccountProductionMasterSection, Integer> {
    List<AccountProductionMasterSection> findAllByApVersion(Integer apVersion);
}

