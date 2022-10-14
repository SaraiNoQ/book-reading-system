package com.dms.guyiyao.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.http.HttpHeaders;

@Configuration
public class ElasticsearchConfig {
//    @Value("${spring.elasticsearch.username}")
//    private  String username;
//    @Value("${spring.elasticsearch.password}")
//    private  String password;
    @Bean
    RestHighLevelClient elasticsearchClient() {
        HttpHeaders headers=new HttpHeaders();
//        headers.setBasicAuth(username,password);
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo("10.6.50.239")
                .withDefaultHeaders(headers).build()
                //.withConnectTimeout(Duration.ofSeconds(5))
                //.withSocketTimeout(Duration.ofSeconds(3))
                //.useSsl()
                //.withDefaultHeaders(defaultHeaders)
                //.withBasicAuth(username, password)
                // ... other options
                ;
        RestHighLevelClient client = RestClients.create(configuration).rest();
        return client;
    }
}
