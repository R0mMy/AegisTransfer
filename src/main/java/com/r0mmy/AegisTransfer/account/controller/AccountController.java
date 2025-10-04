package com.r0mmy.AegisTransfer.account.controller;



import com.r0mmy.AegisTransfer.account.model.Account;
import com.r0mmy.AegisTransfer.account.service.AccountService;
import com.r0mmy.AegisTransfer.account.service.dto.AccountRequest;
import com.r0mmy.AegisTransfer.account.service.dto.AccountResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    private AccountResponse createAccount(@RequestBody AccountRequest accountRequest) {
        return accountService.convertToDTO(accountService.createAccount(accountRequest.getClientId(),
                accountRequest.getInitialBalance(), accountRequest.getCurrency()));
    }

    @GetMapping("/{id}")
    private AccountResponse getAccount(@PathVariable long id) {
        return accountService.convertToDTO(accountService.getAccount(id));
    }
//    REST эндпоинты:
//    POST /api/accounts - создание счёта
//    GET /api/accounts/{id} - получение счёта по ID
//    GET /api/accounts/client/{clientId} - все счета клиента
//    POST /api/accounts/{id}/deposit - пополнение счёта
//    POST /api/accounts/{id}/withdraw - списание средств
//    DELETE /api/accounts/{id} - закрытие счёта


    @GetMapping("/client/{clientId}")
    private List<AccountResponse> getClientAccounts(@PathVariable String clientId) {
        List<Account> list =  accountService.getClientAccounts(clientId);

     return list.stream()
             .map(accountService::convertToDTO)  // ← преобразование каждого элемента
             .collect(Collectors.toList());
    }


}
