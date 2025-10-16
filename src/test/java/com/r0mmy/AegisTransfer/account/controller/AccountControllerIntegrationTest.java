package com.r0mmy.AegisTransfer.account.controller;

import com.r0mmy.AegisTransfer.account.service.dto.AccountRequest;
import com.r0mmy.AegisTransfer.account.service.dto.AccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AccountControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void createAccountTest() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCurrency("USD");
        accountRequest.setClientId("user123");
        accountRequest.setInitialBalance(new BigDecimal(1000.00));

        HttpEntity<AccountRequest> httpRequest = new HttpEntity<>(accountRequest);

        ResponseEntity<AccountResponse> response =  restTemplate.postForEntity(
                "/api/accounts/create", httpRequest, AccountResponse.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getBalance()).isEqualTo(new BigDecimal(1000.00));
        assertThat(response.getBody().getClientId()).isEqualTo("user123");
        assertThat(response.getBody().getCurrency()).isEqualTo("USD");
    }

    @Test
    public void  getAccountTest() {
        AccountRequest accountRequest = new AccountRequest();

        accountRequest.setCurrency("USD");
        accountRequest.setClientId("user123");
        accountRequest.setInitialBalance(new BigDecimal(1000.00));

        ResponseEntity<AccountResponse> createResponse = restTemplate.postForEntity(
                "/api/accounts/create", accountRequest, AccountResponse.class);

        ResponseEntity<AccountResponse> response = restTemplate.getForEntity(
                "/api/accounts/" + createResponse.getBody().getId(), AccountResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getClientId()).isEqualTo("user123");
        assertThat(response.getBody().getCurrency()).isEqualTo("USD");
        assertThat(response.getBody().getBalance()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(response.getBody().getId()).isEqualTo(createResponse.getBody().getId());
    }

}
