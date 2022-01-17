package com.example.config;

import com.example.bo.SocketBO;
import com.example.bo.UdpConfigBO;
import com.example.common.util.MyHexUtil;
import com.example.service.UdpDecodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author: dj
 * @create: 2019-10-28 15:04
 * @description:
 */
@Slf4j
@Configuration
public class UdpConfig {

    @Resource
    private UdpConfigBO udpConfigBO;

    @Resource
    private UdpDecodeService udpDecodeService;

    /**
     * UDP消息接收服务
     * 实例化一个udp
     */
    @Bean
    public IntegrationFlow getUnicastReceivingChannelAdapter() {
        int port = udpConfigBO.getPort();
        int poolSize = udpConfigBO.getPoolSize();
        return IntegrationFlows.from(Udp.inboundAdapter(port).poolSize(poolSize)).channel("udp").get();
    }

    /**
     * 转换器
     */
    @Transformer(inputChannel = "udp", outputChannel = "udpString")
    public SocketBO transformer(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        SocketBO bo = null;
        try {
            Object portObj = headers.get("ip_port");
            if (null != portObj) {
                int port = (int) portObj;
                String address = String.valueOf(headers.get("ip_address"));
                String data = MyHexUtil.bytes2Hex((byte[]) message.getPayload());
                bo = new SocketBO(address, port, data, null);
            }
        } catch (Exception e) {
            log.warn("转换器错误", e);
        }
        return bo;
    }

    /**
     * 过滤器
     */
    @Filter(inputChannel = "udpString", outputChannel = "udpFilter")
    public boolean filter(SocketBO bo) {
        //上传设备协议的数据过滤器
        return null != bo;
    }

    /**
     * 路由分发处理器
     */
    @Router(inputChannel = "udpFilter")
    public String routing(SocketBO bo) {
        String in = bo.getIn();
        if (in.contains("1")) {
            return "udpRoute1";
        } else {
            return "udpRoute2";
        }
    }

    /**
     * 最终处理器1
     */
    @ServiceActivator(inputChannel = "udpRoute1")
    public void udpMessageHandle(SocketBO bo) {
        udpDecodeService.unpack(bo);
    }

    /**
     * 最终处理器2
     */
    @ServiceActivator(inputChannel = "udpRoute2")
    public void udpMessageHandle2(SocketBO bo) {
        udpDecodeService.unpack(bo);
    }

}