package com.github.mbmll.starter.ftp;


import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FTPClientFactory {
    private ConcurrentMap<String, FTPClientPool> pools = new ConcurrentHashMap<>();

    public CloseableFTPClient getClient(FTPProperties properties) throws IOException, FTPConnectionException {
        FTPClientPool ftpClientPool = pools.computeIfAbsent(getKey(properties), s -> new FTPClientPool());
        return ftpClientPool.getConnection(properties);
    }

    private String getKey(FTPProperties properties) {
        return properties.getHostname() + ":" + properties.getPort();
    }

}
