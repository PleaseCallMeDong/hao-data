package com.example.service;

import com.example.bo.SocketBO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dj
 * @date 2022-01-19 16:01
 * @description
 **/
@Slf4j
@Component
public class UdpDecoderHandler extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> out) {
        ByteBuf byteBuf = datagramPacket.content();
        String msg = ByteBufUtil.hexDump(byteBuf).toUpperCase();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        log.info("{}收到消息{}:" + msg);
        out.add(new SocketBO(datagramPacket.duplicate().sender().getAddress().getHostAddress(),
                datagramPacket.duplicate().sender().getPort(), msg, null));
    }
}
