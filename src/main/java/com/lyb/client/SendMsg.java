package com.lyb.client;

import com.lyb.util.LybString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/15 11:04
 * @Description:
 */
public class SendMsg {

    public static final String msg = "我是客户端,当前时间[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]";;

    public static void sendMsg(ChannelHandlerContext ctx) {
        while (true) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //给服务器发消息
            ctx.channel().writeAndFlush(LybString.strToByteBuf(msg));

        }
    }
}
