package com.erp.hr.repository;

import com.erp.common.entity.PerformanceReview;
import com.erp.common.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

    Optional<PerformanceReview> findByReviewCode(String reviewCode);

    List<PerformanceReview> findByEmployeeId(Long employeeId);

    List<PerformanceReview> findByStatus(ReviewStatus status);

    Optional<PerformanceReview> findByEmployeeIdAndPeriod(Long employeeId, String period);

    @Query("SELECT pr FROM PerformanceReview pr WHERE " +
           "(:employeeId IS NULL OR pr.employee.id = :employeeId) AND " +
           "(:status IS NULL OR pr.status = :status) AND " +
           "(:period IS NULL OR pr.period = :period) AND " +
           "(:department IS NULL OR pr.employee.department = :department)")
    Page<PerformanceReview> searchReviews(@Param("employeeId") Long employeeId,
                                           @Param("status") ReviewStatus status,
                                           @Param("period") String period,
                                           @Param("department") String department,
                                           Pageable pageable);

    @Query("SELECT AVG(pr.rating) FROM PerformanceReview pr WHERE pr.employee.department = :department AND pr.status = 'COMPLETED'")
    Optional<Double> avgRatingByDepartment(@Param("department") String department);
}

