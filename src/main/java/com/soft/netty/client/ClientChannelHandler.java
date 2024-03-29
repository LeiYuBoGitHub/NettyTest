package com.soft.netty.client;

import com.soft.netty.entity.decoder.DecoderByte;
import com.soft.netty.common.config.Constant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author 野性的呼唤
 * @Date: 2018/9/13 15:13
 * @Description:
 */
public class ClientChannelHandler extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel sc) {
        // 心跳检测 300S
        sc.pipeline().addLast(new IdleStateHandler(Constant.TIME_OUT,0,0, TimeUnit.SECONDS));
        // 解码器
        sc.pipeline().addLast(new DecoderByte());
        // 处理网络IO
        sc.pipeline().addLast(new NettyClientHandler());
    }
}
