package com.github.mbmll.starter.ftp;


import lombok.Builder;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

@Builder
public class FTPClientPool {
    private final ReentrantLock lock = new ReentrantLock();

    public CloseableFTPClient getConnection(FTPProperties properties) throws IOException, FTPConnectionException {
        // wait
        if (!lock.tryLock()) {
            throw new FTPConnectionException("connection pool is fulled");
        }
        CloseableFTPClient client = new CloseableFTPClientWrapper();
        client.setControlEncoding(StandardCharsets.UTF_8.displayName());
        client.setAutodetectUTF8(true);

        client.connect(properties.getHostname(), properties.getPort());
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            throw new FTPConnectionException(client.getReplyString());
        }
        if (!client.login(properties.getUsername(), properties.getPassword())) {
            throw new FTPConnectionException("login failed");
        }
        return client;
    }

    private class CloseableFTPClientWrapper extends CloseableFTPClient {
        @Override
        public void close() throws IOException {
            super.close();
            lock.unlock();
        }
    }
}
