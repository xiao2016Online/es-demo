package com.xiaopy.esdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaopeiyu
 * @since 2021/2/1
 */

@Configuration
@Slf4j
public class RestHighLevelClientConfig {


    @Bean
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("117.73.11.147", 9200));
        // 设置失败监听器
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                log.error("error:host={},name={}", node.getHost(), node.getName());
            }
        });
        // 设置超时时间
        builder.setRequestConfigCallback(builder1 -> builder1.setConnectTimeout(60 * 1000)
                .setSocketTimeout(60 * 1000)
                .setConnectTimeout(60 * 1000));
        return new RestHighLevelClient(builder);
    }

}
