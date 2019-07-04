package com.lyb.server;

import com.lyb.config.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:22
 * @Description:
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(NettyServerHandler.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        logger.info("**********服务器消息**********");

        Message message = (Message) msg;

        logger.info("收到消息实际内容是:" + new String(message.getData()));

    }

    public void channelRegistered(ChannelHandlerContext ctx) {
        ctx.fireChannelRegistered();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
