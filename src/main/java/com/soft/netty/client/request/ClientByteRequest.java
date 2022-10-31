package com.soft.netty.client.request;

import com.soft.netty.client.send.ClientRegisterSend;

/**
 * @author 野性的呼唤
 * @date 2022/10/29 20:32
 * @description
 */
public class ClientByteRequest {

    public static byte[] register(String account) {
        ClientRegisterSend clientRegisterSend = new ClientRegisterSend();
        clientRegisterSend.setAccount(account);
        return clientRegisterSend.cover();
    }
}
