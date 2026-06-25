package com.erp.hr.repository;

import com.erp.common.entity.TaxSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxSummaryRepository extends JpaRepository<TaxSummary, Long> {

    Optional<TaxSummary> findByTaxCode(String taxCode);

    Optional<TaxSummary> findByEmployeeIdAndYear(Long employeeId, Integer year);

    List<TaxSummary> findByEmployeeId(Long employeeId);

    List<TaxSummary> findByYear(Integer year);

    Page<TaxSummary> findByYear(Integer year, Pageable pageable);

    @Query("SELECT ts FROM TaxSummary ts WHERE " +
           "(:year IS NULL OR ts.year = :year) AND " +
           "(:department IS NULL OR ts.employee.department = :department) AND " +
           "(:w2Issued IS NULL OR ts.w2Issued = :w2Issued)")
    Page<TaxSummary> searchTaxSummaries(@Param("year") Integer year,
                                         @Param("department") String department,
                                         @Param("w2Issued") Boolean w2Issued,
                                         Pageable pageable);
}

