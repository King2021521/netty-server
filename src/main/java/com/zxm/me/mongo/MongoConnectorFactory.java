package com.zxm.me.mongo;

/**
 * @Author zxm
 * @Description
 * @Date Create in 上午 9:31 2018/9/19 0019
 */
public class MongoConnectorFactory {
    private static MongoConnector mongoConnector;

    private MongoConnectorFactory(){}

    public static MongoConnector getInstance(MongoUri uri){
        if(null==mongoConnector){
            synchronized (MongoConnectorFactory.class){
                if(null==mongoConnector){
                    mongoConnector = new MongoConnector(uri);
                }
            }
        }
        return mongoConnector;
    }
}
