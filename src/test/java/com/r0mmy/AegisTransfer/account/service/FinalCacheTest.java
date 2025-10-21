package com.r0mmy.AegisTransfer.account.service;

import com.r0mmy.AegisTransfer.account.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinalCacheTest {

    @Autowired
    private AccountService accountService;

    @Test
    void finalCacheTest() {
        System.out.println("=== ВЫЗОВ 1 ===");
        Account account1 = accountService.getAccount(1L);
        System.out.println("Найден счет: " + account1.getId());

        System.out.println("=== ВЫЗОВ 2 ===");
        Account account2 = accountService.getAccount(1L);
        System.out.println("Найден счет: " + account2.getId());

        System.out.println("=== ВЫЗОВ 3 ===");
        Account account3 = accountService.getAccount(1L);
        System.out.println("Найден счет: " + account3.getId());
    }
}