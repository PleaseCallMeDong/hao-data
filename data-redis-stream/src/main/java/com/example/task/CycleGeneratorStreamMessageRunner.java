package com.example.task;

import com.example.service.StreamProducer;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author dj
 * @date 2022-01-18 16:08
 * @description
 **/
@Component
@AllArgsConstructor
public class CycleGeneratorStreamMessageRunner implements ApplicationRunner {

    private final StreamProducer streamProducer;

    @Override
    public void run(ApplicationArguments args) {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(streamProducer::sendRecord,
                        0, 10, TimeUnit.SECONDS);
    }
}
