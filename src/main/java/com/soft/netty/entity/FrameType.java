package com.soft.netty.entity;

/**
 * @author 野性的呼唤
 * @date 2020/12/2 13:38
 */
public enum FrameType {

    /**
     * 注册上报
     */
    REGISTER_REPORT((byte)0x00),
    /**
     * 注册回复
     */
    REGISTER_REPLY((byte) 0x01),

    HEARTBEAT_REPORT((byte) 0x02),

    HEARTBEAT_REPLY((byte) 0x03);

    private byte data;

    FrameType(byte data) {
        this.data = data;
    }

    public static FrameType getFrameType(byte data) {
        for (FrameType enums : FrameType.values()) {
            if (enums.getData() == data) {
                return enums;
            }
        }
        return null;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

}
