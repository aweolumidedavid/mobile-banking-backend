package com.aod.aod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponseDTO {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo; //using composition
}
