package com.r0mmy.AegisTransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AegisTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(AegisTransferApplication.class, args);
	}


}
