package com.fusion.bank.wallet.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class DescryptoPayload {

    // Conteudo da chave de criptografia do payload
    @Value("${encrypt.key}")
    private String passwordEncrypt;
    @Value("${encrypt.hash}")
    private String passwordDecrypt;


    // Esse bean é responsável por um metedo em especifico no FusionSqlConsumer no injetor ObjectMapper.
    // Além de ser responsável pela compreensão de datas no Json.
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


    // Chave de criptografia do payload
    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(passwordEncrypt, passwordDecrypt);
    }
}
