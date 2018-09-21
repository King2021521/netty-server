package com.zxm.me.handler.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.List;

/**
 * @Author zxm
 * @Description
 * @Date Create in 下午 3:28 2018/9/21 0021
 */
public interface RepositoryTemplate<T,R> {
    ResultSet insert(T t);

    List<Row> query(R r);

    ResultSet delete(R r);
}
