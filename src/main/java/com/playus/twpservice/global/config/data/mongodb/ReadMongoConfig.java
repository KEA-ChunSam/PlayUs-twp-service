package com.playus.twpservice.global.config.data.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.playus.twpservice.domain.party.repository.read",
        mongoTemplateRef = "readMongoTemplate"
)
public class ReadMongoConfig {
}
