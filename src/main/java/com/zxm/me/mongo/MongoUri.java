package com.zxm.me.mongo;

/**
 * @Author zxm
 * @Description
 * @Date Create in 下午 5:20 2018/9/18 0018
 */
public class MongoUri {
    private String host;

    private int port;

    public MongoUri(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        if ("".equals(host)) {
            throw new RuntimeException("host is not blank");
        }
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port < 0 || port > 65535) {
            throw new RuntimeException("port range is 0 to 65535");
        }
        this.port = port;
    }
}
