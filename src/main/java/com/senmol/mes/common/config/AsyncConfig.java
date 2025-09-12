package com.senmol.mes.common.config;

import com.senmol.mes.system.entity.Async;
import com.senmol.mes.system.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Administrator
 */
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 获取属性文件中对应的值，如果属性文件中没有这个属性，则获取赋予的默认值，如默认核心线程数为4
     */
    @Value("${async.executor.thread.core_pool_size:10}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size:60}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity:9999}")
    private int queueCapacity;
    @Value("${async.executor.thread.name_prefix:async_task_}")
    private String namePrefix;
    @Value("${async.executor.thread.await_termination_seconds:5}")
    private Integer awaitTerminationSeconds;

    @Resource
    private AsyncService asyncService;

    /**
     * 自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
     */
    @Primary
    @Override
    @Bean("executor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);
        // 配置关闭时等待任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 配置等待终止时长
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable e, Method method, Object... args) -> {
                log.error("异步运行方法出错, method:{}, args:{}, message:{}", method, Arrays.deepToString(args),
                        e.getMessage(), e);

                this.asyncService.save(
                        new Async(method.toString(), Arrays.deepToString(args),  e.getCause().getMessage(), LocalDateTime.now())
                );
        };
    }
}
