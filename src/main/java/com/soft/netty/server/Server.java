package com.soft.netty.server;

import com.soft.netty.common.config.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;

/**
 * 支持 TCP 协议的 Netty 服务端
 * @author 野性的呼唤
 * @Date: 2018/9/12 18:17
 * @Description:
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        // 启动服务
        start();
    }

    private static void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                // BACKLOG用于构造服务端套接字ServerSocket对象
                // 标识当服务器请求处理线程全满时
                // 用于临时存放已完成三次握手的请求的队列的最大长度
                // 如果未设置或所设置的值小于 1 Java将使用默认值 50
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 是否启用心跳保活机制
                // 在双方TCP套接字建立连接后(即都进入ESTABLISHED状态)并且在两个小时左右上层没有任何数据传输的情况下 这套机制才会被激活
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 在TCP/IP协议中 无论发送多少数据 总是要在数据前面加上协议头 同时 对方接收到数据 也需要发送ACK表示确认
                // 为了尽可能的利用网络带宽 TCP总是希望尽可能的发送足够大的数据 这里就涉及到一个名为Nagle的算法
                // 该算法的目的就是为了尽可能发送大块数据 避免网络中充斥着许多小数据块
                // TCP_NODELAY就是用于启用或关闭Nagle算法
                // 如果要求高实时性 有数据发送时就马上发送 就将该选项设置为true
                // 关闭Nagle算法 如果要减少发送次数减少网络交互 就设置为false等累积一定大小后再发送 默认为false
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
                logger.info("[启动Netty服务成功] / 端口号:" + Constant.PORT);
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
