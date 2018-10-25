package com.lyb.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 协议编码器
 * @Auther: 野性的呼唤
 * @Date: 2018/9/13 11:49
 * @Description:
 */
public class SmartCarEncoder extends MessageToByteEncoder<SmartCarProtocol> {

    protected void encode(ChannelHandlerContext channelHandlerContext, SmartCarProtocol msg, ByteBuf out) {
        // 写入消息SmartCar的具体内容
        // 1.写入消息的开头的信息标志(int类型)
        out.writeInt(msg.getHeadData());
        // 2.写入消息的长度(int 类型)
        out.writeInt(msg.getContentLength());
        // 3.写入消息的内容(byte[]类型)
        out.writeBytes(msg.getContent());
    }
}
