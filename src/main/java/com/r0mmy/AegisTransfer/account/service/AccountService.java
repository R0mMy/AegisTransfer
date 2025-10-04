package com.r0mmy.AegisTransfer.account.service;

import com.r0mmy.AegisTransfer.account.exception.AccountBlockedException;
import com.r0mmy.AegisTransfer.account.exception.AccountNotFoundException;
import com.r0mmy.AegisTransfer.account.model.Account;
import com.r0mmy.AegisTransfer.account.model.AccountTransaction;
import com.r0mmy.AegisTransfer.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

    }

    // Создание нового счёта
    private Account createAccount(String clientId, BigDecimal initBalance, String currency) {
        Account account = new Account();

        account.setClientId(clientId);
        account.setBalance(initBalance);
        account.setStatus(Account.AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setCurrency(currency);

        return accountRepository.save(account);
    }

    // Получение информации о счёте
    private Account getAccount(long id) {
        return accountRepository.getAccountById(id);
    }

    // Получение всех счетов клиента
    private List<Account> getClientAccounts(String clientId) {
        return accountRepository.getAccountsByClientId(clientId);
    }

    // Закрытие счёта
   private void closeAccount(long id) {
        accountRepository.getAccountById(id).setStatus(Account.AccountStatus.CLOSED);
    }


    // Пополнение счёта
    private Account deposit(long id, BigDecimal amount) {
      Account account = accountRepository.getAccountById(id);
      account.setBalance(account.getBalance().add(amount));
      return accountRepository.save(account);
    }

    // Списание средств
    private Account withDraw(long id, BigDecimal amount) {
        Account account = accountRepository.getAccountById(id);
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

    // Проверка достаточности средств
    private boolean hasSufficientFunds(long accountId, BigDecimal amount) {
        Account account = accountRepository.getAccountById(accountId);
        if (amount.compareTo(BigDecimal.ZERO) >=0)
            return false;
        return account.getBalance().compareTo(amount) >=0;
    }

    // Валидация счёта (активен ли, существует ли)
    private void validate (long accountId) {
        accountRepository.findById(accountId).orElseThrow(()-> new AccountNotFoundException("Счёт не найден!"));
        if (accountRepository.getAccountById(accountId).getStatus() != Account.AccountStatus.ACTIVE)
           throw new AccountBlockedException("Счёт заблокирован или закрыт");

    }

    // Получение истории операций (когда добавим AccountTransaction)
    List<AccountTransaction> getAccountHistory(Long accountId) {
        return null;
    }


}
