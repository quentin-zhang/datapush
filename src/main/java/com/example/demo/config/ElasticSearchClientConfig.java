package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClientConfig {
//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        RestHighLevelClient client =  new RestHighLevelClient(
//                RestClient.builder(new HttpHost("192.168.1.45", 10200),new HttpHost("192.168.1.50", 10200),new HttpHost("192.168.1.230", 10200)));
//        return client;
//    }
}