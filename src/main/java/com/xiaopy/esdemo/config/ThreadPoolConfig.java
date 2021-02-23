package com.xiaopy.esdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定义了全局线程池，供项目使用
 * <p>
 *
 * @author xiaopeiyu
 * @since 2019/7/1
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor taskThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数目
        executor.setCorePoolSize(16);
        // 指定最大线程数
        executor.setMaxPoolSize(32);
        // 线程名称前缀
        executor.setThreadNamePrefix("My_Service_ThreadPool_");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        // 对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        return executor;
    }
}
