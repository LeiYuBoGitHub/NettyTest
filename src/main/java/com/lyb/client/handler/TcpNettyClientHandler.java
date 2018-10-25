package com.lyb.client.handler;

import com.lyb.config.SmartCarProtocol;
import com.lyb.util.LybString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端逻辑处理
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:39
 * @Description:
 */
public class TcpNettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        /*ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务端:" + LybString.bufToString(buf));*/
        SmartCarProtocol body = (SmartCarProtocol) msg;

        String x = new String(body.getContent());
        System.out.println("Client接受的客户端的信息 :" + x);
        System.out.println(body.getContentLength());
        System.out.println(body.getHeadData());

    }


}
