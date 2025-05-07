package com.playus.twpservice.domain.config.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.playus.twpservice.domain.chat.repository",
        mongoTemplateRef = "chatMongoTemplate"
)
public class ChatMongoConfig {
}
