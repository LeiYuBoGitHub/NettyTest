package com.soft.netty;

import com.soft.netty.common.util.ByteUtil;

/**
 * @author 野性的呼唤
 * @date 2022/10/31 22:59
 * @description
 */
public class Test {

    public static void main(String[] args) {
        String account = "soft";
        byte[] accountByteArray = account.getBytes();
        String hex = ByteUtil.byteArrayToString(accountByteArray);
    }
}
