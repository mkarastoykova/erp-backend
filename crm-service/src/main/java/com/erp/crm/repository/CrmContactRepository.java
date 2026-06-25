package com.erp.crm.repository;

import com.erp.common.entity.CrmContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CrmContactRepository extends JpaRepository<CrmContact, Long> {
    Optional<CrmContact> findByContactCode(String code);
    boolean existsByContactCode(String code);
    boolean existsByEmail(String email);
    List<CrmContact> findByAccount_Id(Long accountId);

    @Query("SELECT c FROM CrmContact c WHERE " +
           "(:owner IS NULL OR c.owner = :owner) AND " +
           "(:q IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(c.lastName) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(c.company) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(c.email) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<CrmContact> search(@Param("owner") String owner, @Param("q") String q, Pageable pageable);
}

