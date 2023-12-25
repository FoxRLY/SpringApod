package com.example.nasaapod.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceConfig {

    @Bean
    WebClient getWebClient(){
        return WebClient.create();
    }

}
