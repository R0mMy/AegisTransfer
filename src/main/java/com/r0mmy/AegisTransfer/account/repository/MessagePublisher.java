package com.r0mmy.AegisTransfer.account.repository;

public interface MessagePublisher {
    void publish(final String message);
}