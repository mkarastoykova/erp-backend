package com.erp.hr.repository;

import com.erp.common.entity.EmployeeBenefit;
import com.erp.common.enums.BenefitStatus;
import com.erp.common.enums.BenefitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeBenefitRepository extends JpaRepository<EmployeeBenefit, Long> {

    Optional<EmployeeBenefit> findByBenefitCode(String benefitCode);

    List<EmployeeBenefit> findByEmployeeId(Long employeeId);

    List<EmployeeBenefit> findByEmployeeIdAndStatus(Long employeeId, BenefitStatus status);

    List<EmployeeBenefit> findByType(BenefitType type);

    @Query("SELECT eb FROM EmployeeBenefit eb WHERE " +
           "(:employeeId IS NULL OR eb.employee.id = :employeeId) AND " +
           "(:type IS NULL OR eb.type = :type) AND " +
           "(:status IS NULL OR eb.status = :status)")
    Page<EmployeeBenefit> searchBenefits(@Param("employeeId") Long employeeId,
                                          @Param("type") BenefitType type,
                                          @Param("status") BenefitStatus status,
                                          Pageable pageable);
}

