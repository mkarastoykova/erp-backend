package com.erp.hr.repository;

import com.erp.common.entity.PayrollRecord;
import com.erp.common.enums.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {

    Optional<PayrollRecord> findByPayrollCode(String payrollCode);

    Optional<PayrollRecord> findByEmployeeIdAndMonth(Long employeeId, String month);

    List<PayrollRecord> findByMonth(String month);

    List<PayrollRecord> findByStatus(PayrollStatus status);

    List<PayrollRecord> findByEmployeeId(Long employeeId);

    Page<PayrollRecord> findByMonth(String month, Pageable pageable);

    @Query("SELECT pr FROM PayrollRecord pr WHERE " +
           "(:month IS NULL OR pr.month = :month) AND " +
           "(:status IS NULL OR pr.status = :status) AND " +
           "(:department IS NULL OR pr.employee.department = :department)")
    Page<PayrollRecord> searchPayrollRecords(@Param("month") String month,
                                              @Param("status") PayrollStatus status,
                                              @Param("department") String department,
                                              Pageable pageable);

    @Query("SELECT SUM(pr.netPay) FROM PayrollRecord pr WHERE pr.month = :month AND pr.status = 'PROCESSED'")
    Optional<BigDecimal> sumNetPayByMonth(@Param("month") String month);

    @Query("SELECT pr.employee.department, SUM(pr.baseSalary) FROM PayrollRecord pr " +
           "WHERE pr.month = :month GROUP BY pr.employee.department")
    List<Object[]> sumBaseSalaryByDepartmentAndMonth(@Param("month") String month);
}

