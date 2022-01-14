package com.example.common.util;

import java.nio.ByteBuffer;

/**
 * @author dj
 * @date 2022-01-14 11:34
 * @description
 **/
public class MyUnpackUtil {


    /**
     * byteBuffer 转16进制字符串
     */
    public static String readBuffer2Hex(ByteBuffer byteBuffer) {
        String data = null;
        int len = byteBuffer.limit() - byteBuffer.position();
        byte[] bytes = new byte[len];
        if (!byteBuffer.isReadOnly()) {
            byteBuffer.get(bytes);
            StringBuilder sb = new StringBuilder(bytes.length);
            String sTemp;
            for (byte b : bytes) {
                sTemp = Integer.toHexString(0xFF & b);
                if (sTemp.length() < 2) {
                    sb.append(0);
                }
                sb.append(sTemp.toUpperCase());
            }
            data = sb.toString();
        }
        return data;
    }

}
