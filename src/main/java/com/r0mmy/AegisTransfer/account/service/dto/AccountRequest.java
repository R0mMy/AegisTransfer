package com.r0mmy.AegisTransfer.account.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest { //для создания счета
    @NotBlank(message = "ID клиента не может быть пустым!")
    private String clientId;
    @DecimalMin(value = "0.0", inclusive = false, message = "Баланс не может быть отрицательным!")
    private BigDecimal initialBalance;
    @NotBlank(message = "Валюта не может быть пустой")
    private String currency;
}

