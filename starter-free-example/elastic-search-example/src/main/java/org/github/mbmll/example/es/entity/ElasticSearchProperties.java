package org.github.mbmll.example.es.entity;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/17 12:32
 */
@PropertySource(value = "elasticsearch")
public record ElasticSearchProperties (
     String schema,
     String address,
     Integer connectTimeout,
     Integer socketTimeout,
     Integer connectionRequestTimeout,
     Integer maxConnectNum,
     Integer maxConnectPerRoute){
}