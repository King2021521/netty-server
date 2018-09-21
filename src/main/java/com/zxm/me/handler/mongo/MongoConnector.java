package com.zxm.me.handler.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
* @author zxm
 * mongo连接器
 */
public class MongoConnector {
    private MongoUri mongoUri;

    private MongoClient mongoClient;

    public MongoConnector(MongoUri uri){
        this.mongoUri = uri;
        init();
    }

    private void init(){
        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000); //
        options.socketTimeout(30000);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        options.writeConcern(WriteConcern.SAFE);//

        mongoClient = new MongoClient(new ServerAddress(mongoUri.getHost(),mongoUri.getPort()), options.build());
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
