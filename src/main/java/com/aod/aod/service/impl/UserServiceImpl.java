package com.aod.aod.service.impl;

import com.aod.aod.dto.*;
import com.aod.aod.entity.User;
import com.aod.aod.repository.UserRepository;
import com.aod.aod.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService{
 @Autowired
 UserRepository userRepository;

 @Autowired
 EmailService emailService;

 @Autowired
 TransactionService transactionService;

    @Override
    public BankResponseDTO createAccount(UserRequestDTO userRequestDTO) {
        /**
         * Creating an account is saving a new user into the db.
         * check if user already have an account
         */
        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .middleName(userRequestDTO.getMiddleName())
                .gender(userRequestDTO.getGender())
                .address(userRequestDTO.getAddress())
                .stateOfOrigin(userRequestDTO.getStateOfOrigin())
                .status("ACTIVE")
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequestDTO.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .alternatePhoneNumbers(userRequestDTO.getAlternatePhoneNumbers())
                .build();
        User savedUser = userRepository.save(newUser);

        //send an email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations!, your account has been successfully created.\n Your account details: \n " + "Account name: " +
                        savedUser.getFirstName() + " " +
                        savedUser.getLastName() + ".  Account number: " +
                        savedUser.getAccountNumber())
                .build();
        emailService.sendEmail(emailDetails);
        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getMiddleName())
                        .build())
                .build();
    }

    @Override
    public BankResponseDTO balanceEnquiry(EnquiryRequest enquiryRequest) {
        //check if user exist
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
           return BankResponseDTO.builder()
                   .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                   .accountInfo(null)
                   .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getMiddleName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getMiddleName();
    }

    @Override
    public BankResponseDTO creditAccount(CreditDebitRequest creditDebitRequest) {
        //check if user exist
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExist){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

        //save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .amount(creditDebitRequest.getAmount())
                .accountNumber(userToCredit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getMiddleName())
                        .build())
                .build();
    }

    @Override
    public BankResponseDTO debitAccount(CreditDebitRequest creditDebitRequest) {
        //check if account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExist){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //check the balance
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepository.save(userToDebit);

        //save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("DEBIT")
                .amount(creditDebitRequest.getAmount())
                .accountNumber(userToDebit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getMiddleName())
                        .build())
                .build();
    }

    @Override
    public BankResponseDTO transfer(TransferRequest request) {
        //check if account exists
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExists){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        //check the balance
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //debit the source account
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);
        //save transaction
        TransactionDto debitTransactionDto = TransactionDto.builder()
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .accountNumber(sourceAccountUser.getAccountNumber())
                .build();
        transactionService.saveTransaction(debitTransactionDto);

        String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getMiddleName() + " " + sourceAccountUser.getLastName();
        //send debit alert email
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Debit Alert")
                .messageBody("The sum of " + request.getAmount() + " was deducted from your account. Your current account balance is: " + sourceAccountUser.getAccountBalance())
                .recipient(sourceAccountUser.getEmail())
                .build();
        emailService.sendEmail(debitAlert);

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        //credit the destination account
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        //save transaction
        TransactionDto creditTransactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .accountNumber(destinationAccountUser.getAccountNumber())
                .build();
        transactionService.saveTransaction(creditTransactionDto);

        //send credit alert email
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Credit Alert")
                .messageBody("The sum of " + request.getAmount() + " was credited to your account from " + sourceUserName + "Your current account balance is: " + destinationAccountUser.getAccountBalance())
                .recipient(destinationAccountUser.getEmail())
                .build();
        emailService.sendEmail(creditAlert);

        return BankResponseDTO.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }

    /**
     * Balance enquiry
     * name enquiry
     * credit => one way transaction
     * debit => one way transaction
     * transfer => two-way transaction, it involves both debit and credit
     */
}
