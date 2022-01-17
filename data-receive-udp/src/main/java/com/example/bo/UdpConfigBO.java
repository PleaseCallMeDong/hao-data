package com.example.bo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dj
 * @date 2022-01-14 14:25
 * @description
 **/
@Data
@Component
@ConfigurationProperties(prefix = "udp")
public class UdpConfigBO {

    private Integer port;

    private Integer poolSize;

    private String serviceIp;

    public UdpConfigBO() {
        this.port = 8837;
        this.poolSize = 5;
        this.serviceIp = "127.0.0.1";
    }
}
