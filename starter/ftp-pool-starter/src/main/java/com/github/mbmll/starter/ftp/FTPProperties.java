package com.github.mbmll.starter.ftp;

import lombok.Builder;
import lombok.Data;

/**
 * @Author xlc
 * @Description
 * @Date 2025/7/15 15:58    
 */
@Data
@Builder
public class FTPProperties {
    private String hostname;
    private Integer port;
    private String username;
    private String password;
    private Integer maxConnections;
}

