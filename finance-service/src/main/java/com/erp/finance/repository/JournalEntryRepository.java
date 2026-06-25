package com.erp.finance.repository;

import com.erp.common.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByEntryNumber(String entryNumber);
    boolean existsByEntryNumber(String entryNumber);

    @Query("SELECT e FROM JournalEntry e WHERE " +
           "(:account IS NULL OR e.account = :account) AND " +
           "(:q IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(e.entryNumber) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<JournalEntry> search(@Param("account") String account,
                               @Param("q") String q,
                               Pageable pageable);
}

