package com.lyb.server;

import com.lyb.config.ServerChannelHandler;
import com.lyb.server.handler.TcpNettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 支持TCP协议的Netty服务端
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:17
 * @Description:
 */
public class TcpNettyServer {

    //private static Logger logger = Logger.getLogger(TcpNettyServer.class);

    private int port;

    public TcpNettyServer(int port) {
        this.port = port;
        start();
    }

    public static void main(String[] args) {
        new TcpNettyServer(8888);
    }

    public void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerChannelHandler())//
                .option(ChannelOption.SO_BACKLOG, 1024) // 设置tcp缓冲区 // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); //
                //.childHandler(new ServerChannelHandler());
                /*.option(ChannelOption.SO_BACKLOG, 1024) // 设置tcp缓冲区 // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)*/

        /*bootstrap.childHandler(new ServerChannelHandler() {
            @Override
            public void initChannel(SocketChannel ch) {
                System.out.println("initChannel ch:" + ch);
                ch.pipeline()
                         *//*.addLast("decoder", new HttpRequestDecoder())   // 1
                         .addLast("encoder", new HttpResponseEncoder())  // 2*//*
                        .addLast("aggregator", new HttpObjectAggregator(512 * 1024))    // 消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
                        .addLast("handler", new TcpNettyServerHandler());        // 处理
            }
        });*/
        try {
            ChannelFuture f = bootstrap.bind(port).sync();
            if (f.isSuccess()) {
                //logger.debug("启动Netty服务成功，端口号：" + this.port);
                System.out.println("启动Netty服务成功，端口号：" + this.port);
            }

            // 关闭连接
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //logger.debug("优雅退出,释放线程池资源");
            group.shutdownGracefully();
            group.shutdownGracefully();
        }

    }

}
