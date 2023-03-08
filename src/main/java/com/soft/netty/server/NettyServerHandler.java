package com.soft.netty.server;

import com.soft.netty.common.util.ByteUtil;
import com.soft.netty.common.util.StringUtil;
import com.soft.netty.entity.FrameType;
import com.soft.netty.entity.frame.DecoderByteFrame;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
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
            logger.info("✉✉✉✉✉✉✉✉✉✉服务器收到消息✉✉✉✉✉✉✉✉✉✉");
            DecoderByteFrame decoderByteFrame = (DecoderByteFrame) msg;
            FrameType frameType = FrameType.getFrameType(decoderByteFrame.getMessageType());
            if (frameType == null) {
                logger.error("[★★★★★[未找到命令类型]★★★★★] / 连接ID:{} / 收到业务数据:{}", ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                return;
            }
            boolean flag = true;
            byte[] data = new byte[0];
            switch (frameType) {
                case REGISTER_REPORT -> {
                    logger.info("[★★★★★账号注册★★★★★] / 连接ID:{} / 收到业务数据:{}", ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                    ServerContextManage.registerReport(decoderByteFrame, ctx);
                    data = ServerMessageRoute.registerReport(decoderByteFrame);
                    logger.info("[★★★★★账号注册服务器回复★★★★★] / 账户:{} / 连接ID:{} / 收到业务数据:{} / 回复业务数据:{}",
                            ServerContextManage.getAccount(ctx), ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()), ByteUtil.byteArrayToString(DecoderByteFrame.decoderByteData(data)));
                }
                case HEARTBEAT_REPORT -> {
                    logger.info("[★★★★★心跳消息★★★★★] / 连接ID:{} / 收到业务数据:{}", ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                    data = ServerMessageRoute.heartbeatReport(decoderByteFrame);
                    logger.info("[★★★★★心跳消息服务器回复★★★★★] / 账户:{} / 连接ID:{} / 收到业务数据:{} / 回复业务数据:{}",
                            ServerContextManage.getAccount(ctx), ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()), ByteUtil.byteArrayToString(DecoderByteFrame.decoderByteData(data)));
                }
                default -> {
                    logger.error("[★★★★★未知命令★★★★★] / 连接ID:{} / 收到业务数据:{}", ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                    flag = false;
                }
            }
            if (flag) {
                ctx.writeAndFlush(Unpooled.wrappedBuffer(data));
            } else {
                // 显式调用
                logger.info("[★★★★★释放消息资源★★★★★] / 连接ID:{} / 收到业务数据:{}", ctx.channel().id(), ByteUtil.byteArrayToString(decoderByteFrame.getData()));
                ReferenceCountUtil.release(msg);
            }
        } catch (Exception e) {
            logger.error("[★★★★★服务器消息异常★★★★★] / 连接ID:{} / 异常信息:{}", ctx.channel().id(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        logger.info("[Netty连接注册] / 连接ID:{}", ctx.channel().id());
        ctx.fireChannelRegistered();
    }

    /**
     * 处理程序删除
     * @param ctx ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        logger.info("[客户端断开连接] / 连接ID:{}", ctx.channel().id());
    }

    /**
     * 通道活性
     * @param ctx ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("[Netty连接上线] / 连接ID:{}", ctx.channel().id());
    }

    /**
     * 频道不活跃
     * @param ctx ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("[Netty连接下线] / 连接ID:{}", ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String id = ctx.channel().id().toString();
        String account = ServerContextManage.getAccount(ctx);
        logger.error("[Netty异常捕获] / 连接ID:{} / 账号:{} / 异常内容:{}", ctx.channel().id(), account, cause.getMessage());
        boolean flag = ServerContextManage.close(ctx);
        if (!flag) {
            logger.error("[★★★★★[Netty异常捕获后关闭连接失败]★★★★★] / 连接ID:{} / 账号:{}", id, account);
        } else {
            logger.info("[★★★★★[Netty异常捕获后关闭连接成功]★★★★★] / 连接ID:{} / 账号:{}", id, account);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        String id = ctx.channel().id().toString();
        String account = ServerContextManage.getAccount(ctx);
        if (StringUtil.isBlank(account)) {
            logger.info("[★★★★★心跳检测未找到账号★★★★★] / 连接ID:{}", id);
            return;
        }
        logger.info("[★★★★★心跳检测★★★★★] / 连接ID:{} / 账号:{}", id, account);
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state.equals(IdleState.ALL_IDLE)) {
                boolean flag = ServerContextManage.close(ctx);
                if (!flag) {
                    logger.error("[★★★★★[Netty心跳检测超时后关闭连接失败]★★★★★] / 连接ID:{} / 账号:{}", id, account);
                } else {
                    logger.info("[★★★★★[Netty心跳检测超时后关闭连接成功]★★★★★] / 连接ID:{} / 账号:{}", id, account);
                }
            } else {
                logger.info("[★★★★★心跳检测正常★★★★★] / 连接ID:{} / 账号:{}", id, account);
            }
        } else {
            logger.info("[★★★★★触发心跳检测★★★★★] / 连接ID:{} / 账号:{}", id, account);
        }
    }
}
