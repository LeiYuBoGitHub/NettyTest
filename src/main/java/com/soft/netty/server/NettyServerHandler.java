package com.soft.netty.server;

import com.soft.netty.common.util.ByteUtil;
import com.soft.netty.entity.FrameType;
import com.soft.netty.entity.frame.DecoderByteFrame;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 野性的呼唤
 * @Date: 2018/9/12 18:22
 * @Description:
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            logger.info("✉✉✉✉✉✉✉✉✉✉服务器收到设备消息✉✉✉✉✉✉✉✉✉✉");
            DecoderByteFrame decoderByteFrame = (DecoderByteFrame) msg;
            FrameType frameType = FrameType.getFrameType(decoderByteFrame.getMessageType());
            if (frameType == null) {
                logger.error("未知命令");
                return;
            }
            try {
                boolean flag = true;
                byte[] data = new byte[0];
                switch (frameType) {
                    case REGISTER_REPORT -> {
                        logger.info("★★★★★★★★★★收到[注册]消息★★★★★★★★★★");
                        logger.info("[注册]" + " / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
                        logger.info("数据:" + ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                        data = ServerContextManage.registerReport(decoderByteFrame, ctx);
                    }
                    case HEARTBEAT_REPORT -> {
                        logger.info("★★★★★★★★★★收到[心跳]消息★★★★★★★★★★");
                        logger.info("[心跳] / 账户: "+ ServerContextManage.getConnectInfoKey(ctx) +" / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
                        logger.info("数据:" + ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                        data = ServerContextManage.heartbeatReport();
                    }
                    default -> {
                        logger.info("未知类型数据");
                        flag = false;
                    }
                }
                if (flag) {
                    logger.info("服务器响应长度:" + data.length);
                    logger.info("服务器响应字节字符串:" + ByteUtil.byteArrayToString(data));
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        logger.info("[Netty连接注册] / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
        ctx.fireChannelRegistered();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        logger.info("[客户端断开连接 不做操作] / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
    }

    /**
     * 通道活性
     * @param ctx ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("[Netty连接上线] / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
    }

    /**
     * 频道不活跃
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("[Netty连接下线] / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("[Netty异常捕获] / 账户: "+ ServerContextManage.getConnectInfoKey(ctx) +" / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        logger.info("[Netty心跳检测] / 账户: "+ ServerContextManage.getConnectInfoKey(ctx) +" / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state.equals(IdleState.READER_IDLE)) {
                boolean flag = ServerContextManage.close(ctx);
                if (!flag) {
                    logger.info("[Netty心跳检测超时后关闭连接失败] / 账户: "+ ServerContextManage.getConnectInfoKey(ctx) +" / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id());
                }
            }
        }
    }
}
