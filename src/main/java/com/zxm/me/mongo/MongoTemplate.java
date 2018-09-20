package com.zxm.me.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.netty.channel.ChannelId;
import org.bson.Document;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zxm
 * @Description mongo tools
 * @Date Create in 下午 5:27 2018/9/18 0018
 */
public class MongoTemplate {
    private static final String DATABASE_NAME = "netty";
    private static final String COLLECTION_NAME = "channel_info";

    private MongoClient client;

    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> mongoCollection;

    public MongoTemplate(MongoClient client) {
        this.client = client;
        init();
    }

    private void init() {
        mongoDatabase = client.getDatabase(DATABASE_NAME);
        mongoCollection = mongoDatabase.getCollection(COLLECTION_NAME);
    }

    /**
     * 新增节点
     * @param node
     */
    public void insert(Node node){
        Document document = new Document("channelId",node.getChannelId())
        .append("socketAddress",node.getSocketAddress())
        .append("initTime",node.getInitTime());

        mongoCollection.insertOne(document);
    }

    /**
     * 移除节点
     * @param channelId
     */
    public void remove(ChannelId channelId){
        mongoCollection.deleteOne(Filters.eq("channelId",channelId));
    }

    /**
     * 获取全部节点
     * @return
     */
    public List<Node> queryAll(){
        List<Node> nodes = new ArrayList<>();
        FindIterable<Document> findIterable = mongoCollection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext()){
            Document document = mongoCursor.next();
            Node node = new Node();
            node.setChannelId((ChannelId) document.get("channelId"));
            node.setSocketAddress((SocketAddress) document.get("socketAddress"));
            node.setInitTime(document.getDate("initTime"));
            nodes.add(node);
        }
        return nodes;
    }
}
