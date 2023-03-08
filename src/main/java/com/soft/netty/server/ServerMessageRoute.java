package com.soft.netty.server;

import com.soft.netty.entity.FrameType;
import com.soft.netty.entity.frame.DecoderByteFrame;
import com.soft.netty.entity.frame.EncoderByteFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author 野性的呼唤
 * @Date: 2019/11/19 18:37
 * @Description: 消息路由
 */
public class ServerMessageRoute {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageRoute.class);

    /**
     * 注册
     */
    public static byte[] registerReport(DecoderByteFrame frame) {
        return EncoderByteFrame.encoder(FrameType.REGISTER_REPLY, frame.getData());
    }

    /**
     * 心跳
     * @param frame 框架
     * @return 结果
     */
    public static byte[] heartbeatReport(DecoderByteFrame frame) {
        return EncoderByteFrame.encoder(FrameType.HEARTBEAT_REPLY, frame.getData());
    }

}
