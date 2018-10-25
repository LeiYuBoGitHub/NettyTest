package com.lyb.server;

import com.lyb.config.BaisConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.log4j.Logger;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/14 18:56
 * @Description:
 */
public class StartChannel {

    private static Logger logger = Logger.getLogger(StartChannel.class);

    public static Channel channel;

    public static void starServert(ServerBootstrap bootstrap, NioEventLoopGroup group){
        try {

            ChannelFuture f = bootstrap.bind(BaisConstant.PORT).sync();

            if (f.isSuccess()) {
                System.out.println("启动Netty服务成功，端口号：" + BaisConstant.PORT);
                logger.info("启动Netty服务成功，端口号：" + BaisConstant.PORT);
            }

            // 关闭连接
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            logger.info("优雅退出,释放线程池资源");
            group.shutdownGracefully();
            group.shutdownGracefully();
        }

    }

    public static void closeChannelFuture(ChannelFuture f) {

        if (f.isSuccess()) {
            logger.info("启动Netty服务成功，端口号：" + BaisConstant.PORT);
        }

        // 关闭连接
        try {
            f.channel().closeFuture().sync();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }


    public static void startClient(Bootstrap bootstrap, EventLoopGroup group) {
        try {

            ChannelFuture f = bootstrap.connect(BaisConstant.HOST, BaisConstant.PORT).sync();

            channel = f.channel();

            closeChannelFuture(f);

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }finally {
            //优雅的退出，释放NIO线程组
            logger.info("优雅退出,释放NIO线程组");
            group.shutdownGracefully();
        }
    }
}
