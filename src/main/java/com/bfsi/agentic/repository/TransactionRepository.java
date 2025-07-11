package com.bfsi.agentic.repository;

import com.bfsi.agentic.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
    List<TransactionRecord> findTop5ByUserIdOrderByTimestampDesc(String userId);
}
