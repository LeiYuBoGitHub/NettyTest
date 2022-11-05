package com.soft.netty.client;

import com.soft.netty.common.util.ByteUtil;
import com.soft.netty.entity.FrameType;
import com.soft.netty.entity.frame.DecoderByteFrame;
import com.soft.netty.server.ServerContextManage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端逻辑处理
 * @author 野性的呼唤
 * @Date: 2018/9/12 18:39
 * @Description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));

    private static final int TRY_TIME = 3;

    private static int CURRENT_TRY_TIME = 0;

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
                    case REGISTER_REPLY -> {
                        logger.info("★★★★★★★★★★[注册回复]★★★★★★★★★★" + " / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id() + " / 数据:[" + ByteUtil.byteArrayToString(decoderByteFrame.getData()) + "]");
                        ClientContextManage.registerReport(decoderByteFrame, ctx);
                        flag = false;
                    }
                    case HEARTBEAT_REPLY -> {
                        logger.info("★★★★★★★★★★[心跳回复]★★★★★★★★★★" + " / 连接地址:" + ctx.channel().remoteAddress() + " / 连接ID:" + ctx.channel().id() + " / 数据:[" + ByteUtil.byteArrayToString(decoderByteFrame.getData()) + "]");
                        flag = false;
                    }
                    default -> {
                        logger.info("未知类型数据");
                        flag = false;
                    }
                }
                if (flag) {
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
                if(CURRENT_TRY_TIME <= TRY_TIME){
                    CURRENT_TRY_TIME++;
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }
            }
        }
    }

}
