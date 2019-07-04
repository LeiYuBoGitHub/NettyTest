package com.lyb.decoder;

import com.lyb.config.Message;
import com.lyb.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 协议的解码器
 * @Auther: 野性的呼唤
 * @Date: 2018/9/13 11:52
 * @Description:
 */
public class SmartCarDecoder extends ByteToMessageDecoder {

    private static Logger logger = Logger.getLogger(SmartCarDecoder.class);

    /**
     * 协议开始的标准head_data，int类型，占据4个字节 表示数据的长度contentLength，int类型，占据4个字节.
     */
    private final int BASE_LENGTH = 4 + 4;

    //读取基本字节,包含头和长度信息
    private void readBasisByte(ByteBuf buffer) {

        //读取头
        readByte(buffer);

    }

    //读取长度字节
    private int readLengthByte(ByteBuf buffer) {

        //读取长度
        return readByte(buffer);

    }

    private int readByte(ByteBuf buffer) {
        byte[] d1 = new byte[4];
        buffer.readBytes(d1);
        return ByteUtil.byteArrayToInt(d1);
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {

        logger.info("**********进入解码器开始分析程序*****");

        int length = buffer.readableBytes();

        logger.info("数据字节总长度:" + length);

        //收到数据之后,先判断buffer中可读的数据长度是否大于数据包的基本长度

        while (true) {

            int nowLength = buffer.readableBytes();

            logger.info("当前数据字节长度:" + nowLength);

            if (nowLength >= 8) {

                //标记
                buffer.markReaderIndex();

                //读取基本
                readBasisByte(buffer);

                logger.info("读取完头部信息");

                //读取长度

                int dataLength = readLengthByte(buffer);

                //当基础数据读完之后,看看有没有实际数据
                int dl = buffer.readableBytes();

                //实际数据长度小于数据介绍的长度,不读;
                if (dl < dataLength) {

                    logger.info("当前数据长度小于介绍的长度");

                    //还原读指针的位置
                    buffer.resetReaderIndex();

                    logger.info("还原后的字节长度:" + buffer.readableBytes());

                    break;
                }

                byte[] data = new byte[dataLength];
                buffer.readBytes(data);

                Message message = new Message();
                message.setData(data);
                out.add(message);

            }else{
                logger.info("数据不够基本长度,结束读取!");
                break;
            }

        }
    }
}
