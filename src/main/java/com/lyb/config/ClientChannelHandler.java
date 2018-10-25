package com.lyb.config;

import com.lyb.client.handler.TcpNettyClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/13 15:13
 * @Description:
 */
public class ClientChannelHandler extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel sc) {
        //添加自动以协议的编解码工具
        sc.pipeline().addLast(new SmartCarEncoder());
        sc.pipeline().addLast(new SmartCarDecoder());

        //处理网络IO
        sc.pipeline().addLast(new TcpNettyClientHandler());
    }
}
