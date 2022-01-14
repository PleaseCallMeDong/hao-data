package com.example.service;

import com.example.common.util.MyHexUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dj
 * @date 2021-12-22 09:40
 * @description 主动发送协议
 **/
@Slf4j
@Service
public class TcpService implements MessageProcessor<String> {

    /**
     * 连接的AioSession维护
     * AioSession的map
     */
    private static final Map<String, AioSession> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * tcp主下发方法
     *
     * @param aioSession aioSession
     * @param msg        下发内容
     */
    @Override
    public void process(AioSession aioSession, String msg) {
        val sessionId = aioSession.getSessionID();
        AioSession onlineSession = SESSION_MAP.get(sessionId);
        if (null == onlineSession) {
            return;
        }
        try {
            WriteBuffer writeBuffer = onlineSession.writeBuffer();
            byte[] bytes = MyHexUtil.hex2Bytes(msg);
            writeBuffer.write(bytes);
            writeBuffer.flush();
            log.info("发送id:{},发送数据:{}", sessionId, msg);
        } catch (Exception e) {
            log.error("发送数据错误", e);
        }
    }

    /**
     * tcp链接情况
     *
     * @param aioSession       aioSession
     * @param stateMachineEnum StateMachineEnum
     * @param throwable        throwable
     */
    @Override
    public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        String aioSessionId = aioSession.getSessionID();
        switch (stateMachineEnum) {
            case NEW_SESSION:
                log.info("创建链接客户端:{} ", aioSessionId);
                SESSION_MAP.put(aioSessionId, aioSession);
                break;
            case SESSION_CLOSED:
                log.info("断开客户端链接: {}", aioSessionId);
                SESSION_MAP.remove(aioSessionId);
                break;
            default:
        }
    }
}
