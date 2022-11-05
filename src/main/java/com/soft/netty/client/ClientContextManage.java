package com.soft.netty.client;

import com.soft.netty.common.ConnectInfo;
import com.soft.netty.common.config.Constant;
import com.soft.netty.client.report.ClientRegisterReport;
import com.soft.netty.common.util.StringUtil;
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
        logger.info("★★★★★★★★★★[用户注册]★★★★★★★★★★"
                + " / 账户:" + ClientContextManage.getConnectInfoKey(ctx)
                + " / 连接地址:" + ctx.channel().remoteAddress()
                + " / 连接ID:" + ctx.channel().id());
    }

    public static String getConnectInfoKey(ChannelHandlerContext ctx) {
        Attribute<ConnectInfo> attribute = ctx.channel().attr(ServerContextManage.CHANNEL_ATTR_KEY);
        ConnectInfo connectInfo = attribute.get();
        if (connectInfo != null) {
            return connectInfo.getAccount();
        }
        return null;
    }

    /**
     * 关闭
     */
    public static boolean close(ChannelHandlerContext ctx) {
        String key = getConnectInfoKey(ctx);
        if (StringUtil.isBlank(key)) {
            return false;
        }
        String linkId = ctx.channel().id().toString();
        logger.info("[准备关闭连接] / 连接ID:" + linkId);
        ctx.channel().close();
        logger.info("[连接已关闭] / 连接ID:" + linkId);
        // 删除内存
        Constant.clientConnectInfoMap.remove(key);
        return true;
    }
}
