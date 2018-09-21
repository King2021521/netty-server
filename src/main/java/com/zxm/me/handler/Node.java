package com.zxm.me.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.net.SocketAddress;
import java.util.Date;

/**
 * @Author zxm
 * @Description 节点信息
 * @Date Create in 上午 8:58 2018/9/19 0019
 */
public class Node {
    private ChannelId channelId;
    private String userName;
    private Channel channel;
    private SocketAddress socketAddress;
    private Date initTime;

    public ChannelId getChannelId() {
        return channelId;
    }

    public void setChannelId(ChannelId channelId) {
        this.channelId = channelId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }
}
