package com.erp.hr.repository;

import com.erp.common.entity.LeaveRequest;
import com.erp.common.enums.LeaveStatus;
import com.erp.common.enums.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Optional<LeaveRequest> findByLeaveCode(String leaveCode);

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByType(LeaveType type);

    Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

    @Query("SELECT lr FROM LeaveRequest lr WHERE " +
           "(:employeeId IS NULL OR lr.employee.id = :employeeId) AND " +
           "(:status IS NULL OR lr.status = :status) AND " +
           "(:type IS NULL OR lr.type = :type) AND " +
           "(:department IS NULL OR lr.employee.department = :department)")
    Page<LeaveRequest> searchLeaveRequests(@Param("employeeId") Long employeeId,
                                            @Param("status") LeaveStatus status,
                                            @Param("type") LeaveType type,
                                            @Param("department") String department,
                                            Pageable pageable);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId " +
           "AND lr.status = 'APPROVED' " +
           "AND (lr.startDate BETWEEN :from AND :to OR lr.endDate BETWEEN :from AND :to)")
    List<LeaveRequest> findApprovedLeavesInRange(@Param("employeeId") Long employeeId,
                                                  @Param("from") LocalDate from,
                                                  @Param("to") LocalDate to);

    long countByStatusAndEmployeeDepartment(LeaveStatus status, String department);
}

