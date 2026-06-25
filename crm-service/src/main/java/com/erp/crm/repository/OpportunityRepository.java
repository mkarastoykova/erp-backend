package com.erp.crm.repository;

import com.erp.common.enums.OpportunityStage;
import com.erp.common.entity.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    Optional<Opportunity> findByOpportunityCode(String code);
    boolean existsByOpportunityCode(String code);

    @Query("SELECT o FROM Opportunity o WHERE " +
           "(:stage IS NULL OR o.stage = :stage) AND " +
           "(:owner IS NULL OR o.owner = :owner) AND " +
           "(:q IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(o.accountName) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<Opportunity> search(@Param("stage") OpportunityStage stage,
                              @Param("owner") String owner,
                              @Param("q") String q, Pageable pageable);
}

