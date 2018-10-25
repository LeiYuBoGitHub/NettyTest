package com.lyb.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 协议的解码器
 * @Auther: 野性的呼唤
 * @Date: 2018/9/13 11:52
 * @Description:
 */
public class SmartCarDecoder extends ByteToMessageDecoder {


    /**
     * 协议开始的标准head_data，int类型，占据4个字节 表示数据的长度contentLength，int类型，占据4个字节.
     */
    public final int BASE_LENGTH = 4 + 4;

    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {

        //可读长度必须大于基本长度
        if (buf.readableBytes() >= BASE_LENGTH) {
            //防止socket字节流攻击,防止，客户端传来的数据过大,因为，太大的数据，是不合理的

            //记录包头开始的index
            int beginReader;

            while (true) {
                //获取包头开始的index
                beginReader = buf.readerIndex();
                //标记包头开始的index
                buf.markReaderIndex();
                //读到了协议的开始标志，结束while循环
                if (buf.readInt() == BaisConstant.HEAD_DATA) {
                    break;
                }

                //未读到包头，略过一个字节
                //每次略过，一个字节，去读取，包头信息的开始标记
                buf.resetReaderIndex();
                buf.readByte();

                //当略过，一个字节之后，
                //数据包的长度，又变得不满足
                //此时，应该结束。等待后面的数据到达
                if (buf.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            //消息的长度
            int length = buf.readInt();

            // 判断请求数据包数据是否到齐
            if (buf.readableBytes() < length) {
                // 还原读指针
                buf.readerIndex(beginReader);
                return;
            }

            // 读取data数据
            byte[] data = new byte[length];
            buf.readBytes(data);

            SmartCarProtocol protocol = new SmartCarProtocol(data.length, data);
            out.add(protocol);

        }
    }
}
