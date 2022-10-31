package com.soft.netty.server.request;

import com.soft.netty.server.replay.ServerRegisterReply;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 野性的呼唤
 * @date 2020/12/9 17:13
 */
public class ServerByteRequest {

    /**
     * 注册回复
     * @param reply 参数
     * @return 结果
     */
    public static byte[] registerReply(ServerRegisterReply reply) {
        List<Byte> data = new ArrayList<>();
        // 账户长度
        data.add((byte)reply.getAccountLength());
        byte[] accountByteArray = reply.getAccount().getBytes();
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
