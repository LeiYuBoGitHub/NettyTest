package com.soft.netty.server;

import com.soft.netty.common.ConnectInfo;
import com.soft.netty.common.util.StringUtil;
import com.soft.netty.entity.frame.DecoderByteFrame;
import com.soft.netty.server.report.ServerRegisterReport;
import com.soft.netty.common.config.Constant;
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
     */
    static void registerReport(DecoderByteFrame frame, ChannelHandlerContext ctx) {
        ServerRegisterReport model = new ServerRegisterReport();
        model.cover(frame);
        // 校验
        ConnectInfo connectInfo = Constant.CONNECT_INFO_MAP.get(model.getAccount());
        if (connectInfo != null) {
            if (ctx.channel().id().equals(connectInfo.getChannelHandlerContext().channel().id())) {
                logger.info("[注册中发现旧连接] / 连接ID:{}", ctx.channel().id());
                return;
            }
            logger.info("[注册中发现旧连接应该被删除] / 连接ID:{}", connectInfo.getChannelHandlerContext().channel().id());
            // 删除旧连接
            close(connectInfo.getChannelHandlerContext());
        }
        // 存入会话
        logger.info("[注册中发现新连接] / 连接ID:{}", ctx.channel().id());
        Attribute<ConnectInfo> attribute = ctx.channel().attr(CHANNEL_ATTR_KEY);
        connectInfo = new ConnectInfo();
        connectInfo.setAccount(model.getAccount());
        connectInfo.setChannelHandlerContext(ctx);
        attribute.set(connectInfo);
        // 改变内存里的连接状态
        Constant.CONNECT_INFO_MAP.put(model.getAccount(), connectInfo);
        // 连接ID和对应账号编号存入内存
        Constant.CHANNEL_ID_AND_ACCOUT_MAP.put(ctx.channel().id().toString(), model.getAccount());
    }

    /**
     * 关闭
     */
    public static boolean close(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().toString();
        String account = Constant.CHANNEL_ID_AND_ACCOUT_MAP.get(channelId);
        if (StringUtil.isBlank(account)) {
            logger.error("[连接ID查询账号失败] / 连接ID:{}", ctx.channel().id());
            return false;
        }
        ctx.channel().close();
        // 删除内存
        Constant.CONNECT_INFO_MAP.remove(account);
        // 删除对应连接
        Constant.CHANNEL_ID_AND_ACCOUT_MAP.remove(channelId);
        return true;
    }

    public static String getAccount(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().toString();
        return Constant.CHANNEL_ID_AND_ACCOUT_MAP.get(channelId);
    }

    public static ChannelHandlerContext getChannelHandlerContext(String account) {
        ConnectInfo connectInfo = Constant.CONNECT_INFO_MAP.get(account);
        if (connectInfo == null) {
            logger.error("[账号连接未找到] / 账号:{}", account);
            return null;
        }
        boolean flag = connectInfo.getChannelHandlerContext().channel().isActive();
        if (flag) {
            return connectInfo.getChannelHandlerContext();
        }
        return null;
    }

}
