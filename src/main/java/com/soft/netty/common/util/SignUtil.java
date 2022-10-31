package com.soft.netty.common.util;

/**
 * @author 野性的呼唤
 * @Date 2020/12/11 12:55
 * @Description
 */
public class SignUtil {

    public static void main(String[] args) {

    }

    public static int crc16(byte[] data) {
        byte lbs;
        int crcData = 0xffff;
        int bit = 8;
        for (byte dataByte : data) {
            crcData ^= dataByte & 0xff;
            for (int j = 0; j < bit; j++) {
                lbs = (byte) (crcData & 0x01);
                crcData >>= 1;
                if (lbs == 1) {
                    crcData ^= 0x8408;
                }
            }
        }
        crcData ^= 0xffff;
        return crcData;
    }
}
