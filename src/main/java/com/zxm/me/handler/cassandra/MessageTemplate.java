package com.zxm.me.handler.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.zxm.me.handler.Message;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author zxm
 * @Description
 * @Date Create in 下午 4:49 2018/9/21 0021
 */
public class MessageTemplate implements RepositoryTemplate<Message,String> {
    private Session session;

    public MessageTemplate(Session session) {
        this.session = session;
    }

    @Override
    public ResultSet insert(Message message) {
        Insert insert = QueryBuilder.insertInto(ClusterConfig.DEFAULT_DB_NAME, "message")
                .value("id", message.getId())
                .value("content", message.getContent())
                .value("from_address", message.getFromAddress())
                .value("sender",message.getSender())
                .value("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getCreateTime()));
        ResultSet resultSet = session.execute(insert);
        return resultSet;
    }

    @Override
    public List<Row> query(String s) {
        return null;
    }

    @Override
    public ResultSet delete(String s) {
        return null;
    }
}
