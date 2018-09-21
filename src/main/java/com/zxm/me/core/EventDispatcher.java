package com.zxm.me.core;

import com.zxm.me.handler.Message;
import com.zxm.me.handler.Node;
import com.zxm.me.handler.cassandra.CassandraClusterConnector;
import com.zxm.me.handler.cassandra.MessageTemplate;
import com.zxm.me.handler.cassandra.SessionFactory;
import com.zxm.me.support.MessageHandler;
import com.zxm.me.support.NettyChannelMapping;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Author zxm
 * @Description 业务逻辑异步处理分发器，与netty io线程解耦
 * @Date Create in 下午 3:43 2018/9/20 0020
 */
public class EventDispatcher extends AbstractDispatcher implements Dispatcher{
    private MessageTemplate messageTemplate;

    public EventDispatcher() {
        this.messageTemplate = new MessageTemplate(SessionFactory.getInstance());
    }

    /**
     * 异步push消息到客户端
     *
     * @param userName
     */
    public void process(String userName, String msg) {
        threadPool.submit(() -> {
            send(userName,msg);
        });
    }

    /**
     * 持久化消息
     * @param channel
     * @param msg
     */
    public void save(Channel channel, String msg){
        Message message = new Message();
        message.setId(UUID.randomUUID().toString().replace("-",""));
        message.setContent(msg);
        message.setFromAddress(channel.remoteAddress().toString().replace("/",""));
        message.setSender(NettyChannelMapping.getUserName(channel.id()));
        message.setCreateTime(new Date());
        messageTemplate.insert(message);
    }

    /**
     * 推送消息给客户端
     * @param sender
     * @param msg
     */
    private void send(String sender, String msg) {
        Map<ChannelId, Node> map = NettyChannelMapping.getMapping();
        Set<ChannelId> keySet = map.keySet();
        for (ChannelId id : keySet) {
            Node node = map.get(id);
            node.getChannel().writeAndFlush(buildMessage(sender,msg));
        }
    }

    private String buildMessage(String sender, String content){
        String body = "【" + sender + "】" + new SimpleDateFormat("MM-dd a hh:mm:ss").format(new Date()) + "\r\n" + content;
        return MessageHandler.encode(MessageHandler.MESSAGE,body);
    }
}
