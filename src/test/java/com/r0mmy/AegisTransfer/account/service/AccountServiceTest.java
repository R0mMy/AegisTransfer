package com.r0mmy.AegisTransfer.account.service;

import com.r0mmy.AegisTransfer.account.exception.AccountNotFoundException;
import com.r0mmy.AegisTransfer.account.model.Account;
import com.r0mmy.AegisTransfer.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
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
        when(accountRepository.findById(id)).thenReturn(Optional.of(expectedAccount));

        //Assert
        Account result = accountService.getAccount(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void testGetClientAccounts() {
        //Arrange
        long id = 1L;
        String clientId = "client123";
        BigDecimal balance = new BigDecimal("1000.00");
        String currency = "USD";

        long id1 = 2L;
        String clientId1 = "client123";
        BigDecimal balance1 = new BigDecimal("2000.00");
        String currency1 = "RUB";

        Account expectedAccount = new Account();
        expectedAccount.setId(id);
        expectedAccount.setClientId(clientId);
        expectedAccount.setBalance(balance);
        expectedAccount.setCurrency(currency);

        Account expectedAccount1 = new Account();
        expectedAccount1.setId(id1);
        expectedAccount1.setClientId(clientId1);
        expectedAccount1.setBalance(balance1);
        expectedAccount1.setCurrency(currency1);

        List<Account> list = new ArrayList<>();
        list.add(expectedAccount);
        list.add(expectedAccount1);

        //Act
        when(accountRepository.getAccountsByClientId(clientId)).thenReturn(list);

        //Assert
        List<Account> result = accountService.getClientAccounts(clientId);

        assertThat(list).isNotNull();
        assertThat(list).isEqualTo(result);
    }

    @Test
    public void closeAccount() {
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
        expectedAccount.setStatus(Account.AccountStatus.ACTIVE);

        Account closedAccount = new Account();
        closedAccount.setId(id);
        closedAccount.setClientId(clientId);
        closedAccount.setBalance(balance);
        closedAccount.setCurrency(currency);
        closedAccount.setStatus(Account.AccountStatus.CLOSED);

        //Act
        when(accountRepository.findById(id)).thenReturn(Optional.of(expectedAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(closedAccount);

        accountService.closeAccount(id);

        //Assert
        verify(accountRepository, times(1)).findById(id);

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);

        verify(accountRepository, times(1)).save(argumentCaptor.capture());

        Account result = argumentCaptor.getValue();
        assertThat(result.getStatus()).isEqualTo(Account.AccountStatus.CLOSED);





    }

}
