package com.zxm.me.io;

import com.alibaba.fastjson.JSONObject;
import com.zxm.me.core.EventDispatcher;
import com.zxm.me.core.LoginDispatcher;
import com.zxm.me.support.MessageHandler;
import com.zxm.me.support.NettyChannelMapping;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author zxm
 * @Description 服务端数据读取处理器
 * @Date Create in 下午 2:37 2018/6/15 0015
 */
public class NettyServerReadHandler extends SimpleChannelInboundHandler<String> {
    Logger log = LoggerFactory.getLogger(NettyServerReadHandler.class);

    private EventDispatcher eventDispatcher;
    private LoginDispatcher loginDispatcher;

    public NettyServerReadHandler(){
        eventDispatcher = new EventDispatcher();
        loginDispatcher = new LoginDispatcher();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        channelReadExecute(ctx.channel(),msg);
    }

    private void channelReadExecute(Channel channel, String msg){
        JSONObject payload = MessageHandler.decode(msg);
        JSONObject header = payload.getJSONObject(MessageHandler.HEADER);
        String type = header.getString(MessageHandler.MESSAGE_TYPE);
        if(MessageHandler.MESSAGE.equals(type)){
            eventDispatcher.process(loginDispatcher.getUser(channel),payload.getString(MessageHandler.BODY));
            eventDispatcher.save(channel,payload.getString(MessageHandler.BODY));
            return;
        }
        loginDispatcher.addUser(channel,payload.getString(MessageHandler.BODY));
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
        eventDispatcher.process("系统", "系统提示：[" + loginDispatcher.getUser(ctx.channel()) + "]掉线了");
        loginDispatcher.removeUser(ctx.channel().id());
    }
}
