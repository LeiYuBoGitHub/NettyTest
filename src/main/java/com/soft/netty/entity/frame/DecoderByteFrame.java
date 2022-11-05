package com.soft.netty.entity.frame;

/**
 * @author 野性的呼唤
 * @date 2020/12/1 16:02
 */
public class DecoderByteFrame extends AbstractByteFrame {

    public static byte[] decoderByteData(byte[] frame) {
        int x = 0;
        // 略过头
        x++;
        // 略过类型
        x++;
        int length = frame[x];
        byte[] dataByteArray = new byte[length];
        // 指令
        for (int i = 0; i < length; i++) {
            dataByteArray[i] = frame[x];
            x++;
        }
        return dataByteArray;
    }
}
