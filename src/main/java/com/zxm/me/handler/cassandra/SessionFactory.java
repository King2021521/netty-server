package com.zxm.me.handler.cassandra;

import com.datastax.driver.core.Session;

/**
 * @Author
 * @Description
 * @Date Create in 下午 4:56 2018/9/21 0021
 */
public class SessionFactory {
    private static Session session;

    private SessionFactory(){
    }

    public static Session getInstance(){
        if(null==session){
            synchronized (SessionFactory.class){
                if(null==session){
                    session = new CassandraClusterConnector().getSession();
                }
            }
        }
        return session;
    }
}
