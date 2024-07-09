package com.aod.aod.service.impl;

import com.aod.aod.dto.*;

public interface UserService {
    BankResponseDTO createAccount(UserRequestDTO userRequestDTO);
    BankResponseDTO balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponseDTO creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponseDTO debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponseDTO transfer(TransferRequest request);
}
