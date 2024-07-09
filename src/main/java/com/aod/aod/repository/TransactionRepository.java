package com.aod.aod.repository;

import com.aod.aod.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository  extends JpaRepository<Transaction,String> {
}
