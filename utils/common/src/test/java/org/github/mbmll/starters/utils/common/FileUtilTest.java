package org.github.mbmll.starters.utils.common;

import junit.framework.TestCase;
import org.github.mbmll.starters.utils.common.file.FileChunkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author xlc
 * @Description
 * @Date 2024/12/7 23:51:11
 */
public class FileUtilTest extends TestCase {
    private static final Logger log = LoggerFactory.getLogger(FileUtilTest.class);

    public void testChunkWrite2File() throws IOException {
        File source = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\新建文件夹\\杨晨晨20210609_60c084feea5d9.jpg");
        File dessination = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\新建文件夹\\test1.jpg");
        int chunk = 1024;
        AtomicLong length = new AtomicLong();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        FileChunkUtil.chunkReadFile(source, chunk, data -> {
            long offset = length.getAndAdd(data.length);
            log.info("testChunkReadFile {}, {}", data.length, offset);
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    log.info("Test chunk write 2 file {}, {}", data.length, offset);
                    FileChunkUtil.chunkWriteFile(dessination, offset, data);
                } catch (IOException e) {
                    log.error("error", e);
//                    throw new RuntimeException(e);
                }
            }));
        });
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}