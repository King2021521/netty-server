package com.zxm.me.mongo;

import com.mongodb.MongoClient;

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
        mongoClient = new MongoClient(mongoUri.getHost(),mongoUri.getPort());
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
