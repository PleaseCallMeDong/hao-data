package com.example.config;

import com.example.bo.SocketBO;
import com.example.bo.UdpConfigBO;
import com.example.common.util.MyHexUtil;
import com.example.common.util.MyUnpackUtil;
import com.example.service.UdpDecodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: dj
 * @create: 2019-10-28 15:04
 * @description:
 */
@Slf4j
@Configuration
public class UdpServerConfig {

    @Resource
    private UdpConfigBO udpConfigBO;

    @Resource
    private UdpDecodeService udpDecodeService;

    @Bean
    public UnicastReceivingChannelAdapter getUnicastReceivingChannelAdapter() {
        int port = udpConfigBO.getPort();
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(port);//实例化一个udp 4567端口
        adapter.setOutputChannelName("udp");
        return adapter;
    }

    @Transformer(inputChannel = "udp", outputChannel = "udpString")
    public SocketBO transformer(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String address = String.valueOf(headers.get("ip_address"));
        int port = (int) headers.get("ip_port");
        String data = MyHexUtil.byte2Str((byte[]) message.getPayload());
        return new SocketBO(address, port, data, null);
    }

    @Filter(inputChannel = "udpString", outputChannel = "udpFilter")
    public boolean filter(SocketBO bo) {
        return true;//如果接收数据开头不是abc直接过滤掉
    }

    @Router(inputChannel = "udpFilter")
    public String routing(SocketBO bo) {
        String in = bo.getIn();
        if (in.contains("1")) {
            return "udpRoute1";
        } else {
            return "udpRoute2";
        }
    }


    @ServiceActivator(inputChannel = "udpRoute1")
    public void udpMessageHandle(SocketBO bo) {
        udpDecodeService.unpack(bo);
    }

    @ServiceActivator(inputChannel = "udpRoute2")
    public void udpMessageHandle2(SocketBO bo) {
        udpDecodeService.unpack(bo);
    }

    @Bean
    public TcpNetServerConnectionFactory getServerConnectionFactory() {
        TcpNetServerConnectionFactory serverConnectionFactory = new TcpNetServerConnectionFactory(1234);
        serverConnectionFactory.setSerializer(new ByteArrayRawSerializer());
        serverConnectionFactory.setDeserializer(new ByteArrayRawSerializer());
        serverConnectionFactory.setLookupHost(false);
        return serverConnectionFactory;
    }

    @Bean
    public TcpReceivingChannelAdapter getReceivingChannelAdapter() {
        TcpReceivingChannelAdapter receivingChannelAdapter = new TcpReceivingChannelAdapter();
        receivingChannelAdapter.setConnectionFactory(getServerConnectionFactory());
        receivingChannelAdapter.setOutputChannelName("tcp");
        return receivingChannelAdapter;
    }

    @ServiceActivator(inputChannel = "tcp")
    public void messageHandle(Message<?> message) {
        new String((byte[]) message.getPayload());
    }

}