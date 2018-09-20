package com.zxm.me.core;

import com.zxm.me.mongo.Node;
import com.zxm.me.support.NettyChannelMapping;
import io.netty.channel.Channel;

import java.util.Date;

/**
 * @Author
 * @Description 登录处理器
 * @Date Create in 下午 3:58 2018/9/20 0020
 */
public class LoginDispatcher extends AbstractDispatcher implements Dispatcher {
    /**
     * 获取用户名
     * @param channel
     * @return
     */
    public String getUser(Channel channel) {
        String userName = NettyChannelMapping.getUserName(channel.id());
        if (userName == null) {
            return channel.remoteAddress().toString().replace("/", "");
        }
        return userName;
    }

    public void addUser(Channel channel, String userName){
        Node node = new Node();
        node.setChannelId(channel.id());
        node.setUserName(userName);
        node.setChannel(channel);
        node.setSocketAddress(channel.remoteAddress());
        node.setInitTime(new Date());

        NettyChannelMapping.add(channel.id(), node);
    }
}
