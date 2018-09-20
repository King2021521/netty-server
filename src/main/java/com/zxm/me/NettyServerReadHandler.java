package com.zxm.me;

import com.zxm.me.mongo.Node;
import com.zxm.me.support.NettyChannelMapping;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @Author zxm
 * @Description 服务端数据读取处理器
 * @Date Create in 下午 2:37 2018/6/15 0015
 */
public class NettyServerReadHandler extends SimpleChannelInboundHandler<String> {
    private static final Log log = LogFactory.getLog(NettyServerReadHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.info("客户端传来的数据：" + msg);
        if (!msg.startsWith("$_")) {
            send(getUser(ctx), msg);
        }else {
            Node node = new Node();
            node.setChannelId(ctx.channel().id());
            node.setUserName(msg.replace("$_",""));
            node.setChannel(ctx.channel());
            node.setSocketAddress(ctx.channel().remoteAddress());
            node.setInitTime(new Date());

            NettyChannelMapping.add(ctx.channel().id(), node);
        }
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
        log.info("客户端" + ctx.channel().id() + "已连接。。。。" + ctx.channel().remoteAddress());

        Node node = new Node();
        node.setChannelId(ctx.channel().id());
        node.setChannel(ctx.channel());
        node.setSocketAddress(ctx.channel().remoteAddress());
        node.setInitTime(new Date());

        NettyChannelMapping.add(ctx.channel().id(), node);
        send("系统", "欢迎成员：[" + ctx.channel().remoteAddress().toString().replace("/", "") + "]加入");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.warn("客户端" + ctx.channel().id() + "断开了。。。。");
        send("系统", "系统提示：[" + getUser(ctx) + "]掉线了");
        NettyChannelMapping.remove(ctx.channel().id());
    }

    private void send(String sender, String msg) {
        Map<ChannelId, Node> map = NettyChannelMapping.getMapping();
        Set<ChannelId> keySet = map.keySet();
        for (ChannelId id : keySet) {
            Node node = map.get(id);
            node.getChannel().writeAndFlush("【" + sender + "】" + new SimpleDateFormat("MM-dd a hh:mm:ss").format(new Date()) + "\r\n" + msg);
        }
    }

    private String getUser(ChannelHandlerContext ctx) {
        String userName = NettyChannelMapping.getUserName(ctx.channel().id());
        if(userName==null){
            return ctx.channel().remoteAddress().toString().replace("/", "");
        }
        return userName;
    }
}
