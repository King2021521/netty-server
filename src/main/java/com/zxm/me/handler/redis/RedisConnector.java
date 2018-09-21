package com.zxm.me.handler.redis;

/**
 * @Author
 * @Description
 * @Date Create in 上午 10:44 2018/9/21 0021
 */

import redis.clients.jedis.Jedis;

public class RedisConnector {

    private Jedis jedis;

    public RedisConnector() {
        jedis = new Jedis("127.0.0.1", 6379);
    }
}