package com.soft.netty.server;

import com.soft.netty.common.ConnectInfo;
import com.soft.netty.common.util.StringUtil;
import com.soft.netty.entity.frame.DecoderByteFrame;
import com.soft.netty.server.replay.ServerRegisterReply;
import com.soft.netty.server.report.ServerRegisterReport;
import com.soft.netty.common.config.Constant;
import com.soft.netty.server.request.ServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 野性的呼唤
 * @date 2019/11/20 14:59
 */
public class ServerContextManage {

    private static final Logger logger = LoggerFactory.getLogger(ServerContextManage.class);

    public static final AttributeKey<ConnectInfo> CHANNEL_ATTR_KEY = AttributeKey.valueOf("channelAttr");

    /**
     * 注册回复
     * @param frame 解码器字节帧
     * @param ctx   ctx
     * @return {@link byte[]}
     */
    static byte[] registerReport(DecoderByteFrame frame, ChannelHandlerContext ctx) {
        ServerRegisterReport model = new ServerRegisterReport();
        model.cover(frame);
        Attribute<ConnectInfo> attribute = ctx.channel().attr(CHANNEL_ATTR_KEY);
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setAccount(model.getAccount());
        connectInfo.setChannelHandlerContext(ctx);
        attribute.set(connectInfo);
        // 改变内存里的连接状态
        Constant.serverConnectInfoMap.put(model.getAccount(), connectInfo);
        logger.info("[用户重新注册事件] / 用户:" + connectInfo.getAccount() + " / 内存状态:加载 / 网络连接ID:" + ctx.channel().id());
        ServerRegisterReply reply = new ServerRegisterReply();
        reply.setAccountLength(model.getAccountLength());
        reply.setAccount(model.getAccount());
        return ServerRequest.registerReply(reply.cover());
    }

    /**
     * 心跳回复
     * @return {@link byte[]}
     */
    static byte[] heartbeatReport() {
        return ServerRequest.heartbeatReply();
    }

    /**
     * 关闭
     */
    public static boolean close(ChannelHandlerContext ctx) {
        String key = getConnectInfoKey(ctx);
        if (StringUtil.isBlank(key)) {
            return false;
        }
        boolean flag = nettyClose(ctx);
        if (!flag) {
            return false;
        }
        // 删除内存
        Constant.serverConnectInfoMap.remove(key);
        return true;
    }

    public static String getConnectInfoKey(ChannelHandlerContext ctx) {
        Attribute<ConnectInfo> attribute = ctx.channel().attr(ServerContextManage.CHANNEL_ATTR_KEY);
        ConnectInfo connectInfo = attribute.get();
        if (connectInfo != null) {
            return connectInfo.getAccount();
        }
        return null;
    }

    private static boolean nettyClose(ChannelHandlerContext ctx) {
        String linkId = ctx.channel().id().toString();
        logger.info("[准备关闭连接] / 连接ID:" + linkId);
        ctx.channel().close();
        boolean flag = ctx.channel().isActive();
        if (!flag) {
            logger.info("[连接已关闭] / 连接ID:" + linkId);
            return true;
        }
        logger.error("[连接关闭失败] / 连接ID:" + linkId);
        return false;
    }
}
