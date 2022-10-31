package com.soft.netty.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author 野性的呼唤
 * @date 2019/10/23 15:28
 */
public class ConnectInfo {

    private String account;

    private ChannelHandlerContext channelHandlerContext;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
