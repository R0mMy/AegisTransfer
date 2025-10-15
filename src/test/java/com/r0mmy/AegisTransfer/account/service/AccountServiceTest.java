package com.r0mmy.AegisTransfer.account.service;

import com.r0mmy.AegisTransfer.account.model.Account;
import com.r0mmy.AegisTransfer.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    public void testCreateAccount() {
        //Arrange
        String clientId = "client123";
        BigDecimal balance = new BigDecimal("1000.00");
        String currency = "USD";

        Account expectedAccount = new Account();
        expectedAccount.setId(1L);
        expectedAccount.setClientId(clientId);
        expectedAccount.setBalance(balance);
        expectedAccount.setCurrency(currency);

        //Act
        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);

        //Assert
        Account result = accountService.createAccount(clientId, balance, currency);

        verify(accountRepository, times(1)).save(any(Account.class));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getClientId()).isEqualTo(clientId);
    }

    @Test
    public void testGetAccount() {
        //Arrange
        long id = 1L;
        String clientId = "client123";
        BigDecimal balance = new BigDecimal("1000.00");
        String currency = "USD";

        Account expectedAccount = new Account();
        expectedAccount.setId(id);
        expectedAccount.setClientId(clientId);
        expectedAccount.setBalance(balance);
        expectedAccount.setCurrency(currency);

        //Act
        when(accountRepository.getAccountById(id)).thenReturn(expectedAccount);

        //Assert

        Account result = accountService.getAccount(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

    }

}
