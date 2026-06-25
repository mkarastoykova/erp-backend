package com.erp.crm.repository;

import com.erp.common.enums.CasePriority;
import com.erp.common.enums.CaseStatus;
import com.erp.common.entity.SupportCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SupportCaseRepository extends JpaRepository<SupportCase, Long> {
    Optional<SupportCase> findByCaseCode(String code);
    boolean existsByCaseCode(String code);

    @Query("SELECT c FROM SupportCase c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:priority IS NULL OR c.priority = :priority) AND " +
           "(:q IS NULL OR LOWER(c.subject) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(c.accountName) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(c.caseCode) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<SupportCase> search(@Param("status") CaseStatus status,
                              @Param("priority") CasePriority priority,
                              @Param("q") String q, Pageable pageable);
}

