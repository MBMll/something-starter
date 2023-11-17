package org.github.mbmll.example.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.github.mbmll.example.es.entity.ElasticSearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/17 12:34
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(ElasticSearchProperties properties) {
        // 拆分地址
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = properties.address().split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String port = addr.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port), properties.schema()));
        }
        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(properties.connectTimeout());
            requestConfigBuilder.setSocketTimeout(properties.socketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(properties.connectionRequestTimeout());
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(properties.maxConnectNum());
            httpClientBuilder.setMaxConnPerRoute(properties.maxConnectPerRoute());
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }
}
