package io.cws.config;

import io.netty.util.concurrent.ThreadPerTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//@Configuration
//@EnableAsync
//public class AsyncConfig {
//
//    @Bean
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setThreadNamePrefix("AsyncTask-");
//        taskExecutor.setCorePoolSize(15);
//        taskExecutor.setMaxPoolSize(25);
//        taskExecutor.setQueueCapacity(10);
//        taskExecutor.initialize();
//        return taskExecutor;
//    }
//}
