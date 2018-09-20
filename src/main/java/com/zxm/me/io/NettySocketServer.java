package com.zxm.me.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author zxm
 * @Description netty服务端
 * @Date Create in 下午 2:19 2018/6/15 0015
 */
@ChannelHandler.Sharable
public class NettySocketServer {
    private static final Log log = LogFactory.getLog(NettySocketServer.class);

    private int port;

    public NettySocketServer(int port) throws Exception {
        this.port = port;

        this.init(port);
    }

    private void init(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //保持连接数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //有数据立即发送
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel
                            .pipeline()
                            .addLast(new StringDecoder(), new StringEncoder(), new NettyServerReadHandler());
                        }
                    });

            ChannelFuture f = bootstrap.bind(port).sync();

            if (f.isSuccess()) {
                log.info("long connection started success");
            } else {
                log.error("long connection started fail");
            }


            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
