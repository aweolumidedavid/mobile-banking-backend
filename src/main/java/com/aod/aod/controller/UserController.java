package com.aod.aod.controller;

import com.aod.aod.dto.*;
import com.aod.aod.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(
            summary = "Create A New User",
            description = "This API is use to create a new user and assign an account number to the user."
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status Created"
    )
    @PostMapping
    public BankResponseDTO createAccount(@RequestBody UserRequestDTO userRequestDTO){
        return userService.createAccount(userRequestDTO);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "This API is use to get the user balance account enquiry."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status Success"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponseDTO balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    @Operation(
            summary = "Name Enquiry",
            description = "This API is use to get the user name details with the account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status Success"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    @Operation(
            summary = "Credit User/Account",
            description = "This API is use to credit an account/user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status Success"
    )
    @PostMapping("/credit")
    public BankResponseDTO creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }

    @Operation(
            summary = "Debit User",
            description = "This API is use to debit user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status Success"
    )
    @PostMapping("/debit")
    public BankResponseDTO debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @Operation(
            summary = "Transfer",
            description = "This API is use to transfer to another account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status Success"
    )
    @PostMapping("/transfer")
    public BankResponseDTO transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
}
