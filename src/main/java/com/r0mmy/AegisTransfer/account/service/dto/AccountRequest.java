package com.r0mmy.AegisTransfer.account.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest { //для создания счета
    private String clientId;
    private BigDecimal initialBalance;
    private String currency;
}

