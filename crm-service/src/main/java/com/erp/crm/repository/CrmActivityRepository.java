package com.erp.crm.repository;

import com.erp.common.enums.CrmActivityType;
import com.erp.common.entity.CrmActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CrmActivityRepository extends JpaRepository<CrmActivity, Long> {
    Optional<CrmActivity> findByActivityCode(String code);
    boolean existsByActivityCode(String code);
    List<CrmActivity> findByContact_Id(Long contactId);

    @Query("SELECT a FROM CrmActivity a WHERE " +
           "(:type IS NULL OR a.type = :type) AND " +
           "(:q IS NULL OR LOWER(a.subject) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(a.contactName) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(a.accountName) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<CrmActivity> search(@Param("type") CrmActivityType type, @Param("q") String q, Pageable pageable);
}

