package com.playus.twpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TwpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwpServiceApplication.class, args);
	}

}
