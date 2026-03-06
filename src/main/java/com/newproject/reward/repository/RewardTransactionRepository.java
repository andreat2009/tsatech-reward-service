package com.newproject.reward.repository;

import com.newproject.reward.domain.RewardTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RewardTransactionRepository extends JpaRepository<RewardTransaction, Long> {
    List<RewardTransaction> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @Query("select coalesce(sum(r.pointsDelta), 0) from RewardTransaction r where r.customerId = :customerId")
    Integer sumPointsByCustomerId(@Param("customerId") Long customerId);
}
