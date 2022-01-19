package com.example.service;

import cn.hutool.json.JSONUtil;
import com.example.bo.SocketBO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author dj
 * @date 2022-01-19 16:01
 * @description
 **/
@Slf4j
@Component
public class UdpEncoderHandler extends MessageToMessageEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, List<Object> list) {
        SocketBO bo = (SocketBO) o;
        ByteBuf buf = ctx.alloc().buffer(bo.getOut().length);
        buf.writeBytes(bo.getOut());
        //指定客户端的IP及端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(bo.getIp(), bo.getPort());
        list.add(new DatagramPacket(buf, inetSocketAddress));
        log.info("发送消息{}:", JSONUtil.toJsonStr(bo));
    }

}
