package com.zxm.me.core;

import com.zxm.me.handler.mongo.MongoTemplate;
import com.zxm.me.handler.Node;

/**
 * @Author
 * @Description
 * @Date Create in 下午 4:52 2018/9/20 0020
 */
public class NodeTask implements Runnable{
    private MongoTemplate mongoTemplate;

    private Node node;

    public NodeTask(MongoTemplate mongoTemplate,Node node){
        this.mongoTemplate = mongoTemplate;
        this.node = node;
    }

    @Override
    public void run() {
        mongoTemplate.insert(node);
    }
}
