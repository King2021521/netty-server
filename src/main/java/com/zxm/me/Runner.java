package com.zxm.me;

import com.zxm.me.io.NettySocketServer;

/**
 * @Author zxm
 * @Description netty server 启动器
 * @Date Create in 下午 2:21 2018/9/18 0018
 */
public class Runner {
    public static void main(String[] args) throws Exception{
        int port = 8081;
        new NettySocketServer(port);
    }
}
