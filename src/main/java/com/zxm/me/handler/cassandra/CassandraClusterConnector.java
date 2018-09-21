package com.zxm.me.handler.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;

/**
 * @Author zxm
 * @Description cassandra连接器
 * @Date Create in 下午 3:12 2018/9/21 0021
 */
public class CassandraClusterConnector {
    private Cluster cluster;
    private Session session;

    public CassandraClusterConnector(String host, int port, String dbName) {
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions
                .setNewConnectionThreshold(HostDistance.LOCAL, 32);
        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
        poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, 4);

        cluster = Cluster.builder().addContactPoint(host).withPort(port).withPoolingOptions(poolingOptions).build();
        session = cluster.connect(dbName);
    }

    public CassandraClusterConnector(String dbName) {
        this(ClusterConfig.LOCALHOST, ClusterConfig.DEFAULT_PORT, dbName);
    }

    public CassandraClusterConnector() {
        this(ClusterConfig.LOCALHOST, ClusterConfig.DEFAULT_PORT, ClusterConfig.DEFAULT_DB_NAME);
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
