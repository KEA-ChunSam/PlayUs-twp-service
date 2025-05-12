package com.playus.twpservice.global.config.data.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.playus.twpservice.domain.party.repository.write"
)
@EntityScan(basePackages = "com.playus.twpservice.domain.party.entity")
public class JpaConfig {
}
