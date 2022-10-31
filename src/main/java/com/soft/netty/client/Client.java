package com.soft.netty.client;

import com.soft.netty.common.config.Constant;
import com.soft.netty.client.request.ClientRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 野性的呼唤
 * @Date: 2018/9/12 18:30
 * @Description:
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        // 设置一个多线程循环器
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 启动附注类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    //不延迟，消息立即发送
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientChannelHandler());
            // 连接服务端
            Channel channel = bootstrap.connect(Constant.HOST, Constant.PORT).sync().channel();
            channel.writeAndFlush(Unpooled.wrappedBuffer(ClientRequest.register("soft")));
            for (int i = 0; i < 2000; i++ ) {
                Thread.sleep(1000 * 10);
                channel.writeAndFlush(Unpooled.wrappedBuffer(ClientRequest.heartbeat()));
            }
            channel.closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            logger.info("客户端优雅的释放了线程资源...");
        }
    }

}
