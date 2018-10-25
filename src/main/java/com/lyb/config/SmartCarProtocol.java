package com.lyb.config;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/13 11:40
 * @Description:
 */
public class SmartCarProtocol {

    /**
     * 协议头
     */
    private int headData = BaisConstant.HEAD_DATA;

    /**
     * 消息的长度
     */
    private int contentLength;

    /**
     * 协议内容
     */
    private byte[] content;

    public SmartCarProtocol(int contentLength,byte[] content) {
        this.content = content;
        this.contentLength = contentLength;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setHeadData(int headData) {
        this.headData = headData;
    }

    public int getHeadData() {
        return headData;
    }
}
