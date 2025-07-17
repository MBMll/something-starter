package com.github.mbmll.starter.ftp;


import org.apache.commons.net.ftp.FTPClient;

import java.io.Closeable;
import java.io.IOException;

public class CloseableFTPClient extends FTPClient implements Closeable {

    @Override
    public void close() throws IOException {
        if (isConnected()) {
            logout();
            disconnect();
        }
    }
}
