package com.playus.twp_service.config.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.playus.twp_service.party.repository.read",
        mongoTemplateRef = "readMongoTemplate"
)
public class ReadMongoConfig {
}
