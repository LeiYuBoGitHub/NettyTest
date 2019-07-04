package com.lyb.server;

import com.lyb.config.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * 支持TCP协议的Netty服务端
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:17
 * @Description:
 */
public class Server {

    private static Logger logger = Logger.getLogger(NettyServerHandler.class);

    public static void main(String[] args) {
        //启动服务
        start();
    }

    private static void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                //设置tcp缓冲区
                .option(ChannelOption.SO_BACKLOG, 1024)
                //长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //禁用NAGLE算法
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .localAddress(new InetSocketAddress(Constant.PORT))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sch) {
                        sch.pipeline().addLast(new ServerChannelHandler());
                    }
                });
        try {

            ChannelFuture f = bootstrap.bind().sync();

            if (f.isSuccess()) {
                logger.info("启动Netty服务成功,端口号:" + Constant.PORT);
            }

            // 关闭连接
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            logger.info("优雅退出,释放线程池资源");
        }
    }
}
