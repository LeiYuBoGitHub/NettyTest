package com.lyb.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;

/**
 * @Auther: 野性的呼唤
 * @Date: 2018/9/12 18:23
 * @Description:
 */
public class LybString {

    /**
     * 从ByteBuf中获取信息 使用UTF-8编码返回
     * @param buf
     * @return
     */
    public static String bufToString(ByteBuf buf) {

        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ByteBuf strToByteBuf(String str){

        byte[] req = new byte[0];
        try {
            req = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(req);

        return pingMessage;
    }
}
