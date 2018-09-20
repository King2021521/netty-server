package com.zxm.me.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zxm
 * @Description
 * @Date Create in 下午 3:44 2018/9/20 0020
 */
public abstract class AbstractDispatcher {
    protected static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    protected static final ExecutorService threadPool = Executors.newFixedThreadPool(CORE_POOL_SIZE, new ThreadFactoryBuilder().setDaemon(true).build());
}
