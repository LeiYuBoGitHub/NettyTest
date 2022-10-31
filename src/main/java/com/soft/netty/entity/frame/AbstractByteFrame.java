package com.soft.netty.entity.frame;

/**
 * @author 野性的呼唤
 * @date 2022/7/20 15:51
 * @description 协议基础类
 */
public abstract class AbstractByteFrame {

    /**
     * 头部
     */
    private byte head;

    /**
     * 消息类型
     */
    private byte messageType;

    /**
     * 数据长度
     */
    private byte length;

    /**
     * 数据部分
     */
    private byte[] data;

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
