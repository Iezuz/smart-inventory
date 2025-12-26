package com.smartinventory.reporting.repository;

import com.smartinventory.reporting.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {

    List<TransactionRecord> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT t.productId as productId, SUM(CASE WHEN t.type='IN' THEN t.quantity WHEN t.type='OUT' THEN -t.quantity ELSE 0 END) as balance FROM TransactionRecord t GROUP BY t.productId")
    List<Object[]> calculateBalanceByProduct();
}
