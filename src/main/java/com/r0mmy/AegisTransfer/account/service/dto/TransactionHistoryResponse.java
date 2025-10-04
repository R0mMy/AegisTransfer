package com.r0mmy.AegisTransfer.account.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionHistoryResponse { // для операций
    private BigDecimal amount;
}
