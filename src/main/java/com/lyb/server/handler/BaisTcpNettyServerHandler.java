package com.lyb.server.handler;

import com.lyb.util.LybString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/14 18:51
 * @Description:
 */
public class BaisTcpNettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));

    private static final int TRY_TIMES = 3;

    private int currentTime = 0;

    /**
     * 如果超时则会触发这个方法,用来发送心跳检测
     * @param ctx
     * @param evt
     * @throws Exception
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("循环触发时间："+new Date());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                if(currentTime <= TRY_TIMES){
                    System.out.println("currentTime:"+currentTime);
                    currentTime++;
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }
            }
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //接收客户端消息
        ByteBuf buf = (ByteBuf) msg;

        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);

        for (int i = 0;i<con.length;i++){
            System.out.print(con[i] + " ");
        }
        System.out.println("\n");

        //String clientMsg = LybString.bufToString(buf);
        //System.out.println("服务器接收到消息：" + clientMsg);

        //返回给客户端消息
        String str = "我是服务端,当前时间[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]";
        ctx.writeAndFlush(LybString.strToByteBuf(str));
    }
}
