package com.aod.aod.service.impl;

import com.aod.aod.dto.TransactionDto;
import com.aod.aod.entity.Transaction;
import com.aod.aod.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .accountNumber(transactionDto.getAccountNumber())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully");
    }
}
