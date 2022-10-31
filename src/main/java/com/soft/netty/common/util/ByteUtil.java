package com.soft.netty.common.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author 野性的呼唤
 * @date 2019/7/1 18:11
 */
public class ByteUtil {

    public static byte[] intToByte2(int res) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (res >> 8 & 0xFF);
        targets[1] = (byte) (res & 0xFF);
        return targets;
    }

    public static byte[] intToByte2LowLeft(int res) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (res >> 8 & 0xFF);
        targets[0] = (byte) (res & 0xFF);
        return targets;
    }

    /**
     * int类型 转 4 byte
     */
    public static byte[] intToByte4(int res){
        byte[]bytes=new byte[4];
        bytes[0]=(byte) ((res>>24)&0xff);
        bytes[1]=(byte) ((res>>16)&0xff);
        bytes[2]=(byte) ((res>>8)&0xff);
        bytes[3]=(byte) (res&0xff);
        return bytes;
    }

    /**
     * int类型 转 4 byte
     */
    public static byte[] intToByte4LowLeft(int res){
        byte[]bytes=new byte[4];
        bytes[3]=(byte) ((res>>24)&0xff);
        bytes[2]=(byte) ((res>>16)&0xff);
        bytes[1]=(byte) ((res>>8)&0xff);
        bytes[0]=(byte) (res&0xff);
        return bytes;
    }

    /**
     * int类型 转 8 byte
     */
    public static byte[] intToByte8(int res) {
        byte[] targets = new byte[8];
        for(int i=targets.length-1; i>0; i--) {
            targets[i] = (byte) (res & 0xff);
            res = res >> 8;
        }
        return targets;
    }

    public static byte[] longToByte8(long res) {
        byte[] targets = new byte[8];
        for(int i=targets.length-1; i>0; i--) {
            targets[i] = (byte) (res & 0xff);
            res = res >> 8;
        }
        return targets;
    }


    public static byte[] longToByteArray(long value) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((value >> offset) & 0xff);
        }
        return buffer;
    }

    public static long byteArrayToLong(byte[] byteArray) {
        long  values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8; values|= (byteArray[i] & 0xff);
        }
        return values;
    }

    /**
     * byte[] 转 int
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < bytes.length; i++) {
            int shift = (bytes.length - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * byte[] 转 int
     */
    public static int byteArrayToIntLowLeft(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < bytes.length; i++) {
            int shift = (i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static String byteToString(byte cmd) {
        return Integer.toHexString(cmd & 0xff);
    }

    public static String byteArrayToString(byte[] cmd) {
        return byteArrayToString(cmd, cmd.length);
    }

    public static String byteArrayToString(byte[] cmd, int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ((cmd[i] & 0x000000ff) <= 0x0000000f) {
                str.append("0").append(ByteUtil.byteToString(cmd[i])).append(" ");
            } else {
                str.append(ByteUtil.byteToString(cmd[i])).append(" ");
            }
        }
        return str.toString().trim();
    }

    /**
     * 字符串转byte数组（我猜的）
     */
    public static byte[] stringToByteArray(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase().replaceAll(" ", "");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    public static void main(String[] args) {
        byte[] s =  stringToByteArray("01 00 01 0A");
        System.out.println(Arrays.toString(s));
        String x = byteArrayToString(s);
        System.out.println(x.replaceAll(" ", ""));
    }

    private static byte charToByte(char c) {
        return (byte) ("0123456789" +
                "A" +
                "B" +
                "C" +
                "D" +
                "E" +
                "F").indexOf(c);
    }


    public static byte[] listByteToByte(List<byte[]> list) {
        int byteArrayLength = 0;
        for (byte[] bytes : list) {
            byteArrayLength += bytes.length;
        }
        byte[] byteArray = new byte[byteArrayLength];
        int currentIndex = 0;
        for (byte[] bytes : list) {
            System.arraycopy(bytes, 0, byteArray, currentIndex, bytes.length);
            currentIndex += bytes.length;
        }
        return byteArray;
    }

    public static byte[] readByteToByteArray(byte b) {
        byte[] byteArray = new byte[1];
        byteArray[0] = b;
        return byteArray;
    }

}
