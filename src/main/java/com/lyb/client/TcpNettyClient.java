package com.lyb.client;

import com.lyb.client.handler.TcpNettyClientHandler;
import com.lyb.config.BaisConstant;
import com.lyb.config.ClientChannelHandler;
import com.lyb.config.SmartCarProtocol;
import com.lyb.server.TcpNettyServer;
import com.lyb.util.LybString;
import com.sun.xml.internal.ws.util.StringUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:30
 * @Description:
 */
public class TcpNettyClient {

    public static void main(String[] args) {
        start();
    }

    public static void start() {

        //设置一个多线程循环器
        EventLoopGroup group = new NioEventLoopGroup();
        //启动附注类
        Bootstrap bootstrap = new Bootstrap();

       /* bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new TcpNettyClientHandler());*/
        bootstrap.group(group)//
                .channel(NioSocketChannel.class)//
                .option(ChannelOption.TCP_NODELAY, true)//
                .handler(new ClientChannelHandler());//


        //连接服务
        try {
            //ChannelFuture cf = bootstrap.connect(BaisConstant.HOST, BaisConstant.PORT).sync();
            Channel channel = bootstrap.connect(BaisConstant.HOST, BaisConstant.PORT).sync().channel();

            while (true) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //向服务端发送内容
                //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                //String content = reader.readLine();
                //logger.info("客户端将要发送的数据:" + content);
                //
                //String str = "你好小野";
                //SmartCarProtocol response = new SmartCarProtocol(str.getBytes().length,str.getBytes());
                //channel.writeAndFlush(response);
            }

            // 等待链接关闭
            //cf.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            System.out.println("客户端优雅的释放了线程资源...");
        }
    }
}
