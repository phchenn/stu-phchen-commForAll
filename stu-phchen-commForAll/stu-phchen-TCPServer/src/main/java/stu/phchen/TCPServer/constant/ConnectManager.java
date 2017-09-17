package stu.phchen.TCPServer.constant;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;

public class ConnectManager {
    public static Map<String,Channel> connectManager=new HashMap<String, Channel>();
}