package com.example.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: dj
 * @create: 2019-10-29 16:36
 * @description:
 */
@Data
public class SocketBO implements Serializable {

    private String ip;

    private Integer port;

    private String in;

    private byte[] out;

    public SocketBO(String ip, Integer port, String in, byte[] out) {
        this.ip = ip;
        this.port = port;
        this.in = in;
        this.out = out;
    }
}
