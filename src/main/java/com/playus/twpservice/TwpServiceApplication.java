package com.playus.twpservice;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SecurityScheme(
		name      = "AccessCookie",
		type      = SecuritySchemeType.APIKEY,
		in        = SecuritySchemeIn.COOKIE,
		paramName = "Access"
)
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class TwpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwpServiceApplication.class, args);
	}

}
