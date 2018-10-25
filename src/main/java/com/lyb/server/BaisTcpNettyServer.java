package com.lyb.server;

import com.lyb.server.handler.BaisTcpNettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * 官方标准模型
 * @Auther: 野性的呼唤
 * @Date: 2018/9/14 18:46
 * @Description:
 */
public class BaisTcpNettyServer {

    public static void main(String[] args) {
        start();
    }

    public static void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                //连接数
                .option(ChannelOption.SO_BACKLOG, 1024)
                //不延迟，消息立即发送
                .option(ChannelOption.TCP_NODELAY, true)
                //设置tcp缓冲区
                .option(ChannelOption.SO_BACKLOG, 1024)
                //长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sch) {
                        sch.pipeline().addLast(new BaisTcpNettyServerHandler());
                        //加入5秒心跳检测
                        sch.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                    }
                });
        StartChannel.starServert(bootstrap,group);

    }
}
