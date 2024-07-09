package com.aod.aod.controller;

import com.aod.aod.entity.Transaction;
import com.aod.aod.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    @Autowired
    BankStatement bankStatement;

    @GetMapping("/all")
    public List<Transaction> getTransactions(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate){
        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }
}
