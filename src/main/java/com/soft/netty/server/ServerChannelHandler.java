package com.soft.netty.server;

import com.soft.netty.common.config.Constant;
import com.soft.netty.entity.decoder.DecoderByte;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author 野性的呼唤
 * @Date: 2018/9/13 15:09
 * @Description:
 */
class ServerChannelHandler extends ChannelInitializer<SocketChannel> {

    /**
     * 添加自定义协议的编解码工具
     */
    @Override
    protected void initChannel(SocketChannel sc) {
        // 心跳检测 300S
        sc.pipeline().addLast(new IdleStateHandler(0,0,Constant.TIME_OUT, TimeUnit.SECONDS));
        // 解码器
        sc.pipeline().addLast(new DecoderByte());
        // 处理网络IO
        sc.pipeline().addLast(new NettyServerHandler());
    }
}
