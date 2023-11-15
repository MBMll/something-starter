package org.github.mbmll.starters.minio;

import lombok.Data;

/**
 * @Author xlc
 * @Description
 * @Date 2023/4/27 20:46
 */
@Data
public class MinIOProperties {

    /**
     * address of the server.
     */
    private String endpoint;

    /**
     *
     */
    private String accessKey;

    /**
     *
     */
    private String secretKey;

    /**
     *
     */
    private String region;
}
