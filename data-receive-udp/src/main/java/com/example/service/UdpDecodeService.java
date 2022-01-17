package com.example.service;

import com.example.bo.SocketBO;
import com.example.common.util.MyHexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author dj
 * @date 2022-01-14 15:53
 * @description
 **/
@Slf4j
@Service
public class UdpDecodeService {

    public void unpack(SocketBO bo) {
        String in = bo.getIn();
        //log.info("收到UDP: {}", in);
        this.sendMessage(bo);
    }

    public void sendMessage(SocketBO bo) {
        String message = bo.getIn();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(bo.getIp(), bo.getPort());
        byte[] udpMessage = "12".getBytes();
        DatagramPacket datagramPacket;
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramPacket = new DatagramPacket(udpMessage, udpMessage.length, inetSocketAddress);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        //log.info("发送UDP: {}", message);
    }


}
