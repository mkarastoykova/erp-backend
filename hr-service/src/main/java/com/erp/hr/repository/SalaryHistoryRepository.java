package com.erp.hr.repository;

import com.erp.common.entity.SalaryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {

    Optional<SalaryHistory> findByHistoryCode(String historyCode);

    List<SalaryHistory> findByEmployeeIdOrderByEffectiveDateDesc(Long employeeId);

    Page<SalaryHistory> findByEmployeeId(Long employeeId, Pageable pageable);

    Optional<SalaryHistory> findTopByEmployeeIdOrderByEffectiveDateDesc(Long employeeId);
}

