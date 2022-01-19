package com.example.config;

import com.example.service.UdpDecoderHandler;
import com.example.service.UdpEncoderHandler;
import com.example.service.UdpHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.netty.udp.UdpServer;

import java.time.Duration;

/**
 * @author: dj
 * @create: 2019-10-28 15:04
 * @description:
 */
@Configuration
public class UdpConfig {

    @Bean
    public CommandLineRunner serverRunner(UdpDecoderHandler udpDecoderHandler, UdpEncoderHandler udpEncoderHandler,
                                          UdpHandler udpHandler) {
        return strings -> createUdpServer(udpDecoderHandler, udpEncoderHandler, udpHandler);
    }

    /**
     * 创建UDP Server
     *
     * @param udpDecoderHandler： 用于解析UDP Client上报数据的handler
     * @param udpEncoderHandler： 用于向UDP Client发送数据进行编码的handler
     * @param udpHandler:        用户维护UDP链接的handler
     */
    private void createUdpServer(UdpDecoderHandler udpDecoderHandler, UdpEncoderHandler udpEncoderHandler,
                                 UdpHandler udpHandler) {
        UdpServer.create()
                .handle((in, out) -> {
                    in.receive()
                            .asByteArray()
                            .subscribe();
                    return Flux.never();
                })
                //UDP Server端口
                .port(8881)
                .doOnBound(conn -> conn
                        .addHandler("decoder", udpDecoderHandler)
                        .addHandler("encoder", udpEncoderHandler)
                        .addHandler("handler", udpHandler)
                ) //可以添加多个handler
                .bindNow(Duration.ofSeconds(30));
    }

}