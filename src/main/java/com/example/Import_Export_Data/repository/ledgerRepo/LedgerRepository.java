package com.example.Import_Export_Data.repository.ledgerRepo;

import com.example.Import_Export_Data.DTO.FullLedgerInfoDTO;
import com.example.Import_Export_Data.entity.ledgerEntity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger,Integer> {
    @Query(value = """
    SELECT 
        l.*, 
        g.id AS groupId, g.ledger_name AS groupName, 
        sg.id AS subGroupId, sg.ledger_name AS subGroupName 
    FROM master.ledger l 
    LEFT JOIN master.ledger sg ON l.parent_id = sg.id 
    LEFT JOIN master.ledger g ON sg.parent_id = g.id 
    WHERE l.is_ledger = TRUE
    """, nativeQuery = true)
    List<FullLedgerInfoDTO> findAllLedgerWithFullGroupInfo();

    @Query(value = """
    SELECT 
        l.*, 
        g.id AS groupId, g.ledger_name AS groupName, 
        sg.id AS subGroupId, sg.ledger_name AS subGroupName 
    FROM master.ledger l 
    LEFT JOIN master.ledger sg ON l.parent_id = sg.id 
    LEFT JOIN master.ledger g ON sg.parent_id = g.id 
    WHERE l.id = :id
    """, nativeQuery = true)
    Optional<FullLedgerInfoDTO> findLedgerWithFullGroupInfoById(Integer id);

    @Query("SELECT DISTINCT l.apVersion FROM Ledger l WHERE l.apVersion IS NOT NULL order by apVersion ASC")
    List<String> findDistinctApVersions();

    @Query("SELECT l FROM Ledger l WHERE l.ledgerName = :ledgerName AND l.version = :version AND l.apVersion = :apVersion")
    Optional<Ledger> findByLedgerNameAndVersionAndApVersion(String ledgerName, Integer version, Integer apVersion);

    @Query("SELECT l FROM Ledger l WHERE l.isGroup = true ORDER BY l.ledgerName")
    List<Ledger> findAllGroups();

    @Query("SELECT l FROM Ledger l WHERE l.isSubGroup = true ORDER BY l.ledgerName")
    List<Ledger> findAllSubGroups();
}
