package com.lyb.server.handler;

import com.lyb.config.SmartCarProtocol;
import com.lyb.util.LybString;
import com.sun.xml.internal.fastinfoset.stax.events.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:22
 * @Description:
 */
public class TcpNettyServerHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //获取客户端发来的数据信息
        SmartCarProtocol body = (SmartCarProtocol) msg;

        String x = new String(body.getContent());
        System.out.println("客户端:" + x);
        System.out.println(body.getContentLength());
        System.out.println(body.getHeadData());
        // 会写数据给客户端
        String str = "娴子我喜欢你";
        SmartCarProtocol response = new SmartCarProtocol(str.getBytes().length,str.getBytes());
        // 当服务端完成写操作后，关闭与客户端的连接
        ctx.writeAndFlush(response);
        // .addListener(ChannelFutureListener.CLOSE);

        // 当有写操作时，不需要手动释放msg的引用
        // 当只有读操作时，才需要手动释放msg的引用
        /*ByteBuf buf = (ByteBuf) msg;
        String clientMsg = LybString.bufToString(buf);
        System.out.println("服务器接收到消息：" + clientMsg);
        //logger.info("服务器接收到消息：" + clientMsg);

        ctx.writeAndFlush(LybString.strToByteBuf("Love"));*/
       /* int i=0;
        if(!Util.isEmptyString(clientMsg)&&i<2){
            ctx.writeAndFlush(LybString.strToByteBuf("Love"));
            i++;
        }*/
    }

    public void channelRegistered(ChannelHandlerContext ctx) {
        ctx.fireChannelRegistered();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
