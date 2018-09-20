package com.zxm.me.mongo;

/**
 * @Author zxm
 * @Description
 * @Date Create in 上午 9:27 2018/9/19 0019
 */
public class MongoTemplateFactory {
    private static MongoTemplate mongoTemplate;

    private MongoTemplateFactory(){
    }

    public static MongoTemplate getInstance(MongoUri uri){
        if(null==mongoTemplate){
            synchronized (MongoTemplateFactory.class){
                if(null==mongoTemplate){
                    MongoConnector connector = MongoConnectorFactory.getInstance(uri);
                    mongoTemplate = new MongoTemplate(connector.getMongoClient());
                }
            }
        }
        return mongoTemplate;
    }
}
