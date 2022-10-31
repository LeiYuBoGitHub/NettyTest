package com.soft.netty.server.request;

import com.soft.netty.common.util.ByteUtil;
import com.soft.netty.entity.FrameType;
import com.soft.netty.entity.frame.EncoderByteFrame;
import com.soft.netty.server.replay.ServerRegisterReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author 野性的呼唤
 * @date 2019/11/19 18:04
 */
public class ServerRequest {

    private static final Logger logger = LoggerFactory.getLogger(ServerRequest.class);

    /**
     * 注册回复
     * @return 结果
     */
    public static byte[] registerReply(byte[] data) {
        logger.info("业务字节:" + data.length);
        logger.info("业务字节:" + Arrays.toString(data));
        logger.info("业务字节:" + ByteUtil.byteArrayToString(data));
        return EncoderByteFrame.encoder(FrameType.REGISTER_REPLY, data);
    }

    /**
     * 心跳回复
     * @return 结果
     */
    public static byte[] heartbeatReply() {
        byte[] data = new byte[0];
        logger.info("业务字节:" + data.length);
        logger.info("业务字节:" + Arrays.toString(data));
        logger.info("业务字节:" + ByteUtil.byteArrayToString(data));
        return EncoderByteFrame.encoder(FrameType.HEARTBEAT_REPLY, data);
    }

}
