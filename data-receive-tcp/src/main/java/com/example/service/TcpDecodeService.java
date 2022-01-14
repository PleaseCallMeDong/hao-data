package com.example.service;

import com.example.common.util.MyUnpackUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

/**
 * @author dj
 * @date 2021-12-20 14:06
 * @description 接受数据处理
 **/
@Slf4j
@Service
public class TcpDecodeService implements Protocol<String> {

    /**
     * tcp主接收方法入口
     *
     * @param readBuffer readBuffer
     * @param aioSession aioSession
     * @return 下发内容
     */
    @Override
    public String decode(ByteBuffer readBuffer, AioSession aioSession) {
        String data = MyUnpackUtil.readBuffer2Hex(readBuffer);
        log.info("收到aioSessionId:{},收到数据:{}", aioSession.getSessionID(), data);
        return "data";
    }

}
