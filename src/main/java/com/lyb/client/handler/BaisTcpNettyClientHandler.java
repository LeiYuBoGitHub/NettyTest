package com.lyb.client.handler;

import com.lyb.client.SendMsg;
import com.lyb.server.StartChannel;
import com.lyb.util.LybString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/14 19:29
 * @Description:
 */
public class BaisTcpNettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static Logger logger = Logger.getLogger(BaisTcpNettyClientHandler.class);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf s) {

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        //System.out.println(new String(buf.array()));

        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        for (int i = 0;i<con.length;i++){
            System.out.print(con[i]);
        }
        String data = new String(con);
        //System.out.println();
        //String clientMsg = LybString.bufToString(buf);
        System.out.println("\n客户端接收到的消息：" + data);
    }

    /**
     * 给服务器发送消息
     * @param ctx
     * @throws Exception
     */
    public void channelActive(ChannelHandlerContext ctx) {


        System.out.println("激活时间是："+new Date());
        System.out.println("HeartBeatClientHandler channelActive");

        //ctx.channel().writeAndFlush(LybString.strToByteBuf("Love"));



        //ctx.fireChannelActive();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("停止时间是："+new Date());
        System.out.println("HeartBeatClientHandler channelInactive");
    }
}
