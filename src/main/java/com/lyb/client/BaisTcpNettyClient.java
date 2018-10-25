package com.lyb.client;

import com.lyb.client.handler.BaisTcpNettyClientHandler;
import com.lyb.config.BaisConstant;
import com.lyb.server.StartChannel;
import com.lyb.util.LybString;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/14 19:20
 * @Description:
 */
public class BaisTcpNettyClient {

    private static Logger logger = Logger.getLogger(BaisTcpNettyClient.class);

    public static void main(String[] args) {
        start();
    }

    public static void start() {

        //设置一个多线程循环器
        EventLoopGroup group = new NioEventLoopGroup();
        //启动附注类
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                //设置socket工厂
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sch) {
                        //sch.pipeline().addLast(new StringDecoder());
                        //sch.pipeline().addLast(new StringEncoder());
                        sch.pipeline().addLast(new BaisTcpNettyClientHandler());
                        //客户端心跳,5秒内发送心跳告诉服务器还活着
                        //sch.pipeline().addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
                    }
                });
        //StartChannel.startClient(bootstrap,group);

        try {
            Channel channel = bootstrap.connect(BaisConstant.HOST, BaisConstant.PORT).sync().channel();
            int index = 0;
            int head = BaisConstant.HEAD_DATA;
            byte[] r = new byte[5];
            r[index] = (byte)head;
            r[index++] = (byte)5;
            r[index++] = 5;
            r[index++] = 2;
            r[index++] = 1;
            while (true){
                try {
                    Thread.sleep(3000);

                    ByteBuf pingMessage = Unpooled.buffer();
                    ByteBuf buf = pingMessage.writeBytes(r);
                    channel.writeAndFlush(buf);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
