package com.playus.twpservice.global.config.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

@Configuration
@Profile({"dev", "prod"})  // dev, prod 프로필에서만 사용
public class RedisClusterConfig {

    @Value("${spring.data.redis.cluster.nodes}")
    private String redisClusterNodes;

    @Value("${spring.data.redis.cluster.max-redirects}")
    private int maxRedirects;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();

        String[] nodes = redisClusterNodes.split(",");
        for (String node : nodes) {
            String[] hostPort = node.trim().split(":");
            clusterConfig.clusterNode(hostPort[0], Integer.parseInt(hostPort[1]));
        }

        clusterConfig.setMaxRedirects(maxRedirects);

        return new LettuceConnectionFactory(clusterConfig);
    }

    @Bean
    @Qualifier("chatRedisTemplate")
    public RedisTemplate<String, ChattingMessage> chatRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ChattingMessage> redisTemplate = new RedisTemplate<>();

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        Jackson2JsonRedisSerializer<ChattingMessage> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, ChattingMessage.class);

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);

        return redisTemplate;
    }

    @Bean
    @Qualifier("chatRoomRedisTemplate")
    public RedisTemplate<String, Object> chatRoomRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
