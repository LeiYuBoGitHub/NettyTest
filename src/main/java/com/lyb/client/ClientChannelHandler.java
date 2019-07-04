package com.lyb.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/13 15:13
 * @Description:
 */
public class ClientChannelHandler extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel sc) {
        //添加自动以协议的编解码工具
        //sc.pipeline().addLast(new SmartCarEncoder());
        //sc.pipeline().addLast(new SmartCarDecoder());


        ChannelPipeline pipeline = sc.pipeline();

        //处理网络IO
        pipeline.addLast(new NettyClientHandler());
    }
}
