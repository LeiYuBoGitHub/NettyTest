package com.soft.netty.server.replay;

import com.soft.netty.entity.encoder.AbstractEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 野性的呼唤
 * @date 2022/7/18 17:06
 */
public class ServerRegisterReply extends AbstractEncoder {

    private int accountLength;

    private String account;

    public int getAccountLength() {
        return accountLength;
    }

    public void setAccountLength(int accountLength) {
        this.accountLength = accountLength;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

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
