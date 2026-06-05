package com.fusion.bank.wallet.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

@Configuration
public class ThreadsConfig {

    @Bean(name = "threadsVirtual")
    public Executor threadsVirtual() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
