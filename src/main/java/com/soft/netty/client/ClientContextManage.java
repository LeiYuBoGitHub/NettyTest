package com.soft.netty.client;

import com.soft.netty.common.ConnectInfo;
import com.soft.netty.common.config.Constant;
import com.soft.netty.client.report.ClientRegisterReport;
import com.soft.netty.entity.frame.DecoderByteFrame;
import com.soft.netty.server.ServerContextManage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 野性的呼唤
 * @date 2022/10/31 22:45
 * @description
 */
public class ClientContextManage {

    private static final Logger logger = LoggerFactory.getLogger(ClientContextManage.class);

    public static final AttributeKey<ConnectInfo> CHANNEL_ATTR_KEY = AttributeKey.valueOf("channelAttr");

    static void registerReport(DecoderByteFrame frame, ChannelHandlerContext ctx) {
        ClientRegisterReport model = new ClientRegisterReport();
        model.cover(frame);
        Attribute<ConnectInfo> attribute = ctx.channel().attr(CHANNEL_ATTR_KEY);
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setAccount(model.getAccount());
        connectInfo.setChannelHandlerContext(ctx);
        attribute.set(connectInfo);
        // 改变内存里的连接状态
        Constant.clientConnectInfoMap.put(model.getAccount(), connectInfo);
        logger.info("[客户端注册事件] / 用户:" + connectInfo.getAccount() + " / 内存状态:加载 / 网络连接ID:" + ctx.channel().id());
    }

    public static String getConnectInfoKey(ChannelHandlerContext ctx) {
        Attribute<ConnectInfo> attribute = ctx.channel().attr(ServerContextManage.CHANNEL_ATTR_KEY);
        ConnectInfo connectInfo = attribute.get();
        if (connectInfo != null) {
            return connectInfo.getAccount();
        }
        return null;
    }
}
