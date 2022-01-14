package com.example.bo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author dj
 * @date 2021-12-24 17:17
 * @description
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tcp")
public class TcpConfigBO implements Serializable {

    /**
     * tcp端口
     */
    private Integer port;

}
