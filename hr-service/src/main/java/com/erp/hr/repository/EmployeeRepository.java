package com.erp.hr.repository;

import com.erp.common.entity.Employee;
import com.erp.common.enums.EmployeeStatus;
import com.erp.common.enums.EmploymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    List<Employee> findByDepartment(String department);

    List<Employee> findByStatus(EmployeeStatus status);

    List<Employee> findByEmploymentType(EmploymentType employmentType);

    List<Employee> findByManagerId(Long managerId);

    Page<Employee> findByDepartment(String department, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE " +
           "(:department IS NULL OR e.department = :department) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:search IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(e.position) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Employee> searchEmployees(@Param("department") String department,
                                   @Param("status") EmployeeStatus status,
                                   @Param("search") String search,
                                   Pageable pageable);

    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> countByDepartment();
}

