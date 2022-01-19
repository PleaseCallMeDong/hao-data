package com.example.service;

import cn.hutool.json.JSONUtil;
import com.example.config.RedisStreamConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dj
 * @date 2022-01-18 15:41
 * @description
 **/
@Slf4j
@Component
public class OrderStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        // 消息ID
        RecordId messageId = message.getId();
        // 消息的key和value
        String value = JSONUtil.toJsonStr(message.getValue());
        log.info("StreamMessageListener1  stream message。messageId={}, stream={}, body={}", messageId, message.getStream(), value);
        // 通过RedisTemplate手动确认消息
        this.stringRedisTemplate.opsForStream().acknowledge(RedisStreamConfig.group, message);
    }

}
