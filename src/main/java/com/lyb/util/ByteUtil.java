package com.lyb.util;

/**
 * @Auther: 野性的呼唤
 * @Date: 2019/7/1 18:11
 * @Description:
 */
public class ByteUtil {

    /**
     * int类型 转 4 byte
     */
    public static byte[] intTo4Byte(int res){
        byte[]bytes=new byte[4];
        bytes[0]=(byte) ((res>>24)&0xff);
        bytes[1]=(byte) ((res>>16)&0xff);
        bytes[2]=(byte) ((res>>8)&0xff);
        bytes[3]=(byte) (res&0xff);
        return bytes;
    }

    /**
     * byte[] 转 int
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < bytes.length; i++) {
            int shift = (bytes.length - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }
}
