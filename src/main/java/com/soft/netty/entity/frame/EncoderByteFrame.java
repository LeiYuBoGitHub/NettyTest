package com.soft.netty.entity.frame;

import com.soft.netty.entity.FrameType;
import com.soft.netty.common.config.Constant;

/**
 * @author 野性的呼唤
 * @date 2022/7/20 15:51
 */
public class EncoderByteFrame extends AbstractByteFrame {

    public static byte[] encoder(FrameType frameType, byte[] command) {
        byte[] data = new byte[Constant.HEAD_length + Constant.MESSAGE_TYPE_LENGTH + Constant.DATA_LENGTH + command.length];
        int x = 0;
        // 命令头
        data[x] = (byte) Constant.HEAD;
        x++;
        // 消息类型
        data[x] = frameType.getData();
        x++;
        // 数据长度
        data[x] = (byte) command.length;
        x++;
        // 指令
        for (byte b : command) {
            data[x] = b;
            x++;
        }
        return data;
    }

    private static byte getFrameTypeByte(FrameType frameType) {
        return switch (frameType) {
            case REGISTER_REPORT -> 0xf;
            case REGISTER_REPLY -> 0x10;
            case HEARTBEAT_REPORT -> 0x13;
            case HEARTBEAT_REPLY -> 0x14;
        };
    }
}
