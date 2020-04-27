package com.example.eureka_client_client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * @author Chen
 * @version 1.0
 * @date 2020/4/27 20:01
 * @description:
 */
@RestController
@Configuration
public class AskController {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    @LoadBalanced
//    RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }


    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private RestTemplate mRestTemplate;

    @RequestMapping("/ask")
    public String ask() {
        String body = mRestTemplate.getForEntity("http://eureka-client-service/hello/{name}", String.class, name).getBody();
        return body;
    }


//    @Bean
//    @LoadBalanced
//    RestOperations restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }


}
