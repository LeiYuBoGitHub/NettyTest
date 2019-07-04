package com.lyb.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * 客户端逻辑处理
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:39
 * @Description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(NettyClientHandler.class);

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client active 上线");
        super.channelActive(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client close 客户端关闭");
        super.channelInactive(ctx);

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("客户端");
    }

}
