package com.soft.netty.entity.decoder;

import com.soft.netty.common.util.ByteUtil;
import com.soft.netty.entity.frame.DecoderByteFrame;
import com.soft.netty.common.config.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * @author 野性的呼唤
 * @date : 2019/11/22 16:52
 */
public class DecoderByte extends ByteToMessageDecoder {

    private final static Logger logger = LoggerFactory.getLogger(DecoderByte.class);

    /**
     * 基本长度
     */
    private static final Integer BASIC_LENGTH = 1 + 1 + 1;

    private byte readByte(ByteBuf buffer) {
        return buffer.readByte();
    }

    private byte[] readByteArray(ByteBuf buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        try {
            logger.info("☻☻☻☻☻☻☻☻☻☻进入服务器解码器开始分析程序☻☻☻☻☻☻☻☻☻☻");
            int length = buffer.readableBytes();
            logger.info("请求字节总长度:" + length);
            if (length <= 0) {
                logger.error("请求无字节");
                return;
            }

            // 标记
            buffer.markReaderIndex();
            byte[] requestByte = readByteArray(buffer, length);
            logger.info("请求字节字符串:" + ByteUtil.byteArrayToString(requestByte));
            // 还原读指针的位置
            buffer.resetReaderIndex();

            while (true) {
                int cacheLength = buffer.readableBytes();
                // 当前缓存区字节总长度
                if (cacheLength <= 0) {
                    // 字节没有长度 不予读取
                    break;
                }
                // 不够基础长度 放入缓存
                if (cacheLength < BASIC_LENGTH) {
                    // 字节总长度小于基本长度 不予读取
                    break;
                }
                // 标记
                buffer.markReaderIndex();
                // 检查请求头
                DecoderByteFrame decoderByteFrame = new DecoderByteFrame();
                if (!checkHeadByte(buffer, decoderByteFrame)) {
                    return;
                }
                // 类型
                byte messageType = readByte(buffer);
                decoderByteFrame.setMessageType(messageType);
                // 获取标明数据长度
                if (!checkLength(cacheLength, buffer, decoderByteFrame)) {
                    return;
                }
                // 数据长度
                int lengthInt = decoderByteFrame.getLength();
                // 数据
                byte[] dataByteArray = readByteArray(buffer, lengthInt);
                decoderByteFrame.setData(dataByteArray);
                out.add(decoderByteFrame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkHeadByte(ByteBuf buffer, DecoderByteFrame decoderByteFrame) {
        byte headByte = readByte(buffer);
        if ((int) headByte != Constant.HEAD) {
            logger.error("请求头不匹配 不予读取");
            // 还原读指针的位置
            buffer.resetReaderIndex();
            return false;
        }
        decoderByteFrame.setHead(headByte);
        return true;
    }

    private boolean checkLength(int cacheLength, ByteBuf buffer, DecoderByteFrame decoderByteFrame) {
        byte dataLengthByte = readByte(buffer);
        // 实际过来帧的长度小于基本长度加上帧传递的长度说明帧不够 不予读取
        if (cacheLength < (BASIC_LENGTH + (int) dataLengthByte)) {
            logger.error("请求字节总长度小于标明的长度 不予读取");
            // 还原读指针的位置
            buffer.resetReaderIndex();
            return false;
        }
        decoderByteFrame.setLength(dataLengthByte);
        return true;
    }

}
