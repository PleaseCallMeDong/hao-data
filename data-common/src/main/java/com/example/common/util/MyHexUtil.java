package com.example.common.util;

/**
 * @author dj
 * @date 2022-01-14 11:43
 * @description
 **/
public class MyHexUtil {

    public static String getHex(String hexData, int start, int hexLength) {
        return hexData.substring((start - 1) * 2, (start - 1 + hexLength) * 2);
    }

    /**
     * 16进制字符串转byte
     */
    public static byte[] hex2Bytes(String str) {
        if (str == null || "".equals(str.trim())) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * btye转字符串
     */
    public static String byte2Str(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(b);
        }
        return sb.toString();
    }

}
