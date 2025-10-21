package com.r0mmy.AegisTransfer.account.repository;

import com.r0mmy.AegisTransfer.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long> {

    List<Account> getAccountsByClientId(String clientId);
}
