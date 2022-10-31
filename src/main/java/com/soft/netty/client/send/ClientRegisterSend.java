package com.soft.netty.client.send;

import com.soft.netty.entity.encoder.Encoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 野性的呼唤
 * @date 2022/10/29 0:23
 * @description
 */
public class ClientRegisterSend extends Encoder {

    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public byte[] cover() {
        List<Byte> data = new ArrayList<>();
        byte[] accountByteArray = this.account.getBytes();
        // 账户长度
        data.add((byte)accountByteArray.length);
        for (byte b : accountByteArray) {
            data.add(b);
        }
        byte[] result = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }
}
