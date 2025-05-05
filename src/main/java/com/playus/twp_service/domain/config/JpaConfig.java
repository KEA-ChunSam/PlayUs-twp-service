package com.playus.twp_service.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.playus.twp_service.party.repository.write"
)
@EntityScan(basePackages = "com.playus.twp_service.party.entity")
public class JpaConfig {
}
