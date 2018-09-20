package com.zxm.me;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zxm.me.mongo.Node;
import com.zxm.me.support.MessageHandler;
import com.zxm.me.support.NettyChannelMapping;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zxm
 * @Description 服务端数据读取处理器
 * @Date Create in 下午 2:37 2018/6/15 0015
 */
public class NettyServerReadHandler extends SimpleChannelInboundHandler<String> {
    private static final Log log = LogFactory.getLog(NettyServerReadHandler.class);

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(CORE_POOL_SIZE, new ThreadFactoryBuilder().setDaemon(true).build());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        channelReadExcute(ctx.channel(),msg);
    }

    private void channelReadExcute(Channel channel, String msg){
        JSONObject payload = MessageHandler.decode(msg);
        JSONObject header = payload.getJSONObject(MessageHandler.HEADER);
        String type = header.getString(MessageHandler.MESSAGE_TYPE);
        if(MessageHandler.MESSAGE.equals(type)){
            asyncProcessor(getUser(channel),payload.getString(MessageHandler.BODY));
            return;
        }
        addUser(channel,payload.getString(MessageHandler.BODY));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info("client " + ctx.channel().remoteAddress() + " connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.warn("client " + ctx.channel().remoteAddress() + " interrupted");
        asyncProcessor("系统", "系统提示：[" + getUser(ctx.channel()) + "]掉线了");
        NettyChannelMapping.remove(ctx.channel().id());
    }

    /**
     * 异步业务处理
     *
     * @param userName
     */
    private void asyncProcessor(String userName, String msg) {
        threadPool.submit(() -> {
            send(userName,msg);
        });
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

    /**
     * 获取用户名
     * @param channel
     * @return
     */
    private String getUser(Channel channel) {
        String userName = NettyChannelMapping.getUserName(channel.id());
        if (userName == null) {
            return channel.remoteAddress().toString().replace("/", "");
        }
        return userName;
    }

    private String buildMessage(String sender, String content){
        String body = "【" + sender + "】" + new SimpleDateFormat("MM-dd a hh:mm:ss").format(new Date()) + "\r\n" + content;
        return MessageHandler.encode(MessageHandler.MESSAGE,body);
    }

    private void addUser(Channel channel, String userName){
        Node node = new Node();
        node.setChannelId(channel.id());
        node.setUserName(userName);
        node.setChannel(channel);
        node.setSocketAddress(channel.remoteAddress());
        node.setInitTime(new Date());

        NettyChannelMapping.add(channel.id(), node);
    }
}
