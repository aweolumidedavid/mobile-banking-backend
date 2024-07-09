package com.aod.aod.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "404";
    public static final String ACCOUNT_EXISTS_MESSAGE = "A user already exist with this email";
    public static final String ACCOUNT_CREATION_CODE = "201";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "404";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User does not exist";
    public static final String ACCOUNT_FOUND_CODE = "201";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found!";
    public static final String ACCOUNT_CREDIT_SUCCESS_CODE = "200";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Account credited successfully!";
    public static final String INSUFFICIENT_BALANCE_CODE = "400";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance!";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "200";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account debited successfully";
    public static final String TRANSFER_SUCCESSFUL_CODE = "200";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Account debited successfully";

    public static String generateAccountNumber(){
        /**
         * 2023 + 6 random digits
         */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        //generate random number between min and max
        int randomNumbers = (int) Math.floor(Math.random() * (max - min + 1) + min);

        //convert the current year and the random number to string and concat them
        String year = String.valueOf(currentYear);
        String randomNumberStringValue = String.valueOf(randomNumbers);
        StringBuilder accountNumber =  new StringBuilder();
        return accountNumber.append(year).append(randomNumberStringValue).toString();
    }
}
