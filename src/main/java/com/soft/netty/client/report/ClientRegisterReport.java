package com.soft.netty.client.report;

import com.soft.netty.entity.decoder.AbstractDecoder;
import com.soft.netty.entity.frame.DecoderByteFrame;

/**
 * @author 野性的呼唤
 * @date 2022/7/20 16:18
 */
public class ClientRegisterReport extends AbstractDecoder {

    private int accountLength;

    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountLength() {
        return accountLength;
    }

    public void setAccountLength(int accountLength) {
        this.accountLength = accountLength;
    }

    @Override
    public void cover(DecoderByteFrame frame) {
        byte[] commandByteArray = frame.getData();
        int x = 0;
        int accountLength = commandByteArray[x];
        x++;
        // 用户账号
        byte[] accountByteArray = new byte[accountLength];
        for (int i = 0; i < accountByteArray.length; i++) {
            accountByteArray[i] = commandByteArray[x];
            x++;
        }
        this.accountLength = accountLength;
        this.account = new String(accountByteArray);
    }
}
