package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.example.config.RedisStreamConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author dj
 * @date 2022-01-18 16:09
 * @description
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class StreamProducer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static boolean base = true;

    public void sendRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("time", DateUtil.now());
        ObjectRecord<String, String> record = StreamRecords.newRecord()
                .in(RedisStreamConfig.key)
                .ofObject(jsonObject.toString())
                .withId(RecordId.autoGenerate());
        RecordId recordId = stringRedisTemplate.opsForStream().add(record);
        stringRedisTemplate.opsForStream().trim(RedisStreamConfig.key, 5);
        log.info("add:[{}],返回的record-id:[{}]", jsonObject, recordId);
    }
}
