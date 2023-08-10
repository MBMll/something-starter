package org.github.mbmll.starters.minio;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xlc
 * @Description
 * @Date 2023/4/27 20:43
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
public class MinIOConfiguration {

    /**
     * prefix
     */
    public static final String PREFIX = "minio";

    @Bean
    @ConfigurationProperties(prefix = PREFIX)
    public MinIOProperties properties() {
        return new MinIOProperties();
    }

    @Bean
    @ConditionalOnClass(value = MinioClient.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
    public MinioClient minioClient(MinIOProperties properties) {
        return MinioClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .httpClient(new OkHttpClient())
            .region(properties.getRegion()).build();
    }
}
