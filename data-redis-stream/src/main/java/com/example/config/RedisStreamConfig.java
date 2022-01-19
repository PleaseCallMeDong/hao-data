package com.example.config;

import com.example.service.OrderStreamListener;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author dj
 * @date 2022-01-18 15:40
 * @description
 **/
@Slf4j
@Configuration
public class RedisStreamConfig {

    public static final String key = "stream-003";

    public static final String group = "group-003";
    public static final String myName = "name-003";

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private OrderStreamListener orderStreamListener;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> start() {

        Boolean base = stringRedisTemplate.hasKey(key);
        if (null == base || !base) {
            stringRedisTemplate.opsForStream().createGroup(key, group);
        } else {
            StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(key);
            if (groups.isEmpty()) {
                stringRedisTemplate.opsForStream().createGroup(key, group);
            }
        }

        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                // 一次性最多拉取多少条消息
                .batchSize(10)
                // 执行消息轮询的执行器
                .executor(this.threadPoolTaskExecutor)
                // 消息消费异常的handler
                .errorHandler(t -> log.error("redisStream消费错误:{}", t.getMessage()))
                // 超时时间，设置为0，表示不超时（超时后会抛出异常）
                .pollTimeout(Duration.ZERO)
                // 序列化器
                .serializer(new StringRedisSerializer())
                .build();

        var streamMessageListenerContainer =
                StreamMessageListenerContainer.create(this.redisConnectionFactory, options);

        streamMessageListenerContainer.receive(Consumer.from(group, myName),
                StreamOffset.create(key, ReadOffset.lastConsumed()), this.orderStreamListener);

        streamMessageListenerContainer.start();

        return streamMessageListenerContainer;
    }

}