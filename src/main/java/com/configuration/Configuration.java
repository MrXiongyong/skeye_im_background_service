package com.configuration;

import com.mongodb.MongoClient;
import com.service.HttpTemplate;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lenovo
 * @Title: Configuration
 * @Package com.configuration
 * @Description: Configuration
 * @date 2022/11/10 23:41
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Resource
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Bean
    @Primary
    public HttpTemplate httpTemplate() {
        HttpTemplate httpTemplate = new HttpTemplate(templateBuilder());
        List<HttpMessageConverter<?>> messageConverters = httpTemplate.getMessageConverters();
        if (messageConverters.removeIf(converter -> converter instanceof GsonHttpMessageConverter)) {
            messageConverters.add(mappingJackson2HttpMessageConverter);
        }
        return httpTemplate;
    }

    public HttpTemplate.HttpTemplateBuilder templateBuilder(){
        return HttpTemplate.newBuilder()
                .maxTotal(400).maxPerRoute(100).connectTimeout(10000).socketTimeout(10000);
    }

    @Bean
    public ScheduledLockConfiguration taskScheduler(LockProvider lockProvider) {
        return ScheduledLockConfigurationBuilder
                .withLockProvider(lockProvider)
                .withPoolSize(10)
                .withDefaultLockAtMostFor(Duration.ofMinutes(10))
                .build();
    }

    /**
     * 功能描述：锁信息写入mongo shedlock表
     *
     * @return void
     * @author lihq141
     * @date 2020/7/7 0007 上午 11:28
     */
    @Bean
    public LockProvider lockProvider(MongoClient mongo) {
        return new MongoLockProvider(mongo, "PrometheusConfig");
    }

    /**
     * 异步执行
     * @return 异步执行
     */
    @Bean
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(20);
        //配置最大线程数
        executor.setMaxPoolSize(200);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //设置线程的空闲时间
        executor.setKeepAliveSeconds(60);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }



}
