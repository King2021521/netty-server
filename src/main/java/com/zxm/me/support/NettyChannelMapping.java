package com.zxm.me.support;

import com.zxm.me.mongo.Node;
import io.netty.channel.ChannelId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zxm
 * @Description
 * @Date Create in 下午 4:50 2018/9/18 0018
 */
public class NettyChannelMapping {
    private static Map<ChannelId, Node> mapping = new ConcurrentHashMap<>(1024);

    public static void add(ChannelId id, Node node) {
        mapping.put(id, node);
    }

    public static void remove(ChannelId id) {
        mapping.remove(id);
    }

    public static Map<ChannelId, Node> getMapping() {
        return mapping;
    }

    public static String getUserName(ChannelId id) {
        return mapping.get(id) == null ? null : mapping.get(id).getUserName();
    }
}
