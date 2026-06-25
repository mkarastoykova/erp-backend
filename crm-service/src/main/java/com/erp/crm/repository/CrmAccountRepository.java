package com.erp.crm.repository;

import com.erp.common.enums.AccountType;
import com.erp.common.enums.CrmAccountStatus;
import com.erp.common.entity.CrmAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CrmAccountRepository extends JpaRepository<CrmAccount, Long> {
    Optional<CrmAccount> findByAccountCode(String code);
    boolean existsByAccountCode(String code);

    @Query("SELECT a FROM CrmAccount a WHERE " +
           "(:type IS NULL OR a.type = :type) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:q IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(a.industry) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(a.country) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<CrmAccount> search(@Param("type") AccountType type,
                             @Param("status") CrmAccountStatus status,
                             @Param("q") String q, Pageable pageable);
}

