package com.soft.netty.common.config;

import com.soft.netty.common.ConnectInfo;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 野性的呼唤
 * @date 2022/10/28 22:46
 * @description
 */
public class Constant {

    public static final String HOST = "127.0.0.1";

    public static final int PORT = 8100;

    public static final int TIME_OUT = 300;

    public static final int HEAD = 97;

    public static final int HEAD_length = 1;

    public static final int ACCOUNT_LENGTH = 8;

    public static final int MESSAGE_TYPE_LENGTH = 1;

    public static final int DATA_LENGTH = 1;

    public static Map<String, ConnectInfo> clientConnectInfoMap = new HashMap<>(16);

    /**
     * 连接信息
     */
    public static Map<String, ConnectInfo> CONNECT_INFO_MAP = new HashMap<>(512);

    /**
     * 连接ID和账号
     */
    public static Map<String, String> CHANNEL_ID_AND_ACCOUT_MAP = new HashMap<>(512);
}
