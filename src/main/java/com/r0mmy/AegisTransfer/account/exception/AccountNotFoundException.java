package com.r0mmy.AegisTransfer.account.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long id) {
        super("Аккаунт " + id + " не найден!");
    }
}
