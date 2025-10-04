package com.r0mmy.AegisTransfer.account.service.dto;

import com.r0mmy.AegisTransfer.account.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponse { // для отправки данных
    private Long id;
    private String clientId;
    private BigDecimal balance;
    private String currency;
    private Account.AccountStatus status;
}
