package com.zxm.me.handler.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.zxm.me.handler.Node;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author zxm
 * @Description
 * @Date Create in 下午 3:33 2018/9/21 0021
 */
public class ChannelTemplate implements RepositoryTemplate<Node, ChannelId> {
    private Logger log = LoggerFactory.getLogger(ChannelTemplate.class);

    private Session session;

    public ChannelTemplate(Session session) {
        this.session = session;
    }

    @Override
    public ResultSet insert(Node node) {
        Insert insert = QueryBuilder.insertInto(ClusterConfig.DEFAULT_DB_NAME, "channel_info")
                .value("channel_id", node.getChannelId().asShortText())
                .value("user_name", node.getUserName())
                .value("socket_address", node.getSocketAddress().toString())
                .value("init_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        ResultSet resultSet = session.execute(insert);
        log.info("insert 1 record");
        return resultSet;
    }

    @Override
    public List<Row> query(ChannelId channelId) {
        return null;
    }

    @Override
    public ResultSet delete(ChannelId channelId) {
        Delete.Where delete = QueryBuilder.delete()
                .from(ClusterConfig.DEFAULT_DB_NAME, "channel_info")
                .where(QueryBuilder.eq("channel_id", channelId.asShortText()));
        ResultSet resultSet = session.execute(delete);
        log.info("delete 1 record");
        return resultSet;
    }
}
