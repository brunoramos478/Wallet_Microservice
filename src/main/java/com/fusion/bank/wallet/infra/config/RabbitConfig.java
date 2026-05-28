package com.fusion.bank.wallet.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue walletQueue() {
        return new Queue("wallet-queue", true);
    }
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("wallet-exchange");
    }

    @Bean
    public Binding binding(Queue walletQueue, DirectExchange exchange) {
        return BindingBuilder.bind(walletQueue).to(exchange).with("wallet-routing-key");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
