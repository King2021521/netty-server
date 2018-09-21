package com.zxm.me.core;

import com.zxm.me.handler.Node;
import com.zxm.me.handler.cassandra.CassandraClusterConnector;
import com.zxm.me.handler.cassandra.ChannelTemplate;
import com.zxm.me.handler.cassandra.SessionFactory;
import com.zxm.me.support.NettyChannelMapping;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.Date;

/**
 * @Author
 * @Description 登录处理器
 * @Date Create in 下午 3:58 2018/9/20 0020
 */
public class LoginDispatcher extends AbstractDispatcher implements Dispatcher {
    private ChannelTemplate channelTemplate;

    public LoginDispatcher() {
        channelTemplate = new ChannelTemplate(SessionFactory.getInstance());
    }

    /**
     * 获取用户名
     *
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

    public void addUser(Channel channel, String userName) {
        final Node node = new Node();
        node.setChannelId(channel.id());
        node.setUserName(userName);
        node.setChannel(channel);
        node.setSocketAddress(channel.remoteAddress());
        node.setInitTime(new Date());

        NettyChannelMapping.add(channel.id(), node);

        threadPool.submit(()->channelTemplate.insert(node));
    }

    public void removeUser(ChannelId id) {
        NettyChannelMapping.remove(id);
        threadPool.submit(()->channelTemplate.delete(id));
    }
}
