package com.lyb.client;

import com.lyb.config.Constant;
import com.lyb.util.ByteUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:30
 * @Description:
 */
public class Client {

    private static Logger logger = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        start();
    }

    private static void start() {

        //设置一个多线程循环器
        EventLoopGroup group = new NioEventLoopGroup();

        try {

            //启动附注类
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    //不延迟，消息立即发送
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientChannelHandler());

            //连接服务端
            Channel channel = bootstrap.connect(Constant.HOST, Constant.PORT).sync().channel();

            for (int i = 0; i < 2000; i++ ) {
                channel.writeAndFlush(Unpooled.wrappedBuffer(sendData()));
            }

            channel.closeFuture().sync();

        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            logger.info("客户端优雅的释放了线程资源...");
        }
    }


    private static byte[] sendData() {

        String message = "野性的呼唤";

        int dataLength = message.getBytes().length;

        byte[] dataLengthBytes = ByteUtil.intTo4Byte(dataLength);

        byte[] headBytes = ByteUtil.intTo4Byte(1);

        int length = dataLength + dataLengthBytes.length + headBytes.length;

        byte[] data = new byte[length];

        int x = 0;

        for (int i = 0;i < 4; i++) {
            data[x] = headBytes[i];
            x++;
        }

        for (int i = 0;i < 4; i++) {
            data[x] = dataLengthBytes[i];
            x++;
        }

        for (int i = 0;i < message.getBytes().length; i++) {
            data[x] = message.getBytes()[i];
            x++;
        }

        logger.info("发送的数据总长度:" + length);

        logger.info("发送的字节数组:" + Arrays.toString(data));

        return data;

    }
}
