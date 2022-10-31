package com.soft.netty.client.request;

import com.soft.netty.common.util.ByteUtil;
import com.soft.netty.entity.FrameType;
import com.soft.netty.entity.frame.EncoderByteFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

/**
 * @author 野性的呼唤
 * @date 2022/10/29 20:31
 * @description
 */
public class ClientRequest {

    private static final Logger logger = LoggerFactory.getLogger(ClientRequest.class);

    /**
     * 注册
     * @return 结果
     */
    public static byte[] register(String account) {
        byte[] data = ClientByteRequest.register(account);
        logger.info("业务字节:" + data.length);
        logger.info("业务字节:" + Arrays.toString(data));
        logger.info("业务字节:" + ByteUtil.byteArrayToString(data));
        return EncoderByteFrame.encoder(FrameType.REGISTER_REPORT, data);
    }

    /**
     * 心跳
     * @return 结果
     */
    public static byte[] heartbeat() {
        byte[] data = new byte[0];
        logger.info("业务字节:" + data.length);
        logger.info("业务字节:" + Arrays.toString(data));
        logger.info("业务字节:" + ByteUtil.byteArrayToString(data));
        return EncoderByteFrame.encoder(FrameType.HEARTBEAT_REPORT, data);
    }
}
