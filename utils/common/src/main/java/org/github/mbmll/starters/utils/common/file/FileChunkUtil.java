package org.github.mbmll.starters.utils.common.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Arrays;
import java.util.function.Consumer;

//@Slf4j
public class FileChunkUtil {
    private static final Logger log = LoggerFactory.getLogger(FileChunkUtil.class);

    /**
     * @param destination
     * @param offset
     * @param data
     *
     * @throws IOException
     * @see #chunkReadFile(File, int, Consumer)
     */
    public static void chunkWriteFile(File destination, long offset, byte[] data) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(destination, "rwd");
             FileChannel channel = raf.getChannel()) {
            while (true) {
                try (FileLock ignored = channel.tryLock()) {
                    raf.seek(offset);
                    raf.write(data);
                    break;
                } catch (OverlappingFileLockException e) {
                    log.debug("try lock failed", e);
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            log.error("Error writing to file", e);
        }
    }

    /**
     * @param file
     * @param chunkSize
     * @param consumer
     *
     * @throws IOException
     * @see #chunkWriteFile(File, long, byte[])
     */
    public static void chunkReadFile(File file, int chunkSize, Consumer<byte[]> consumer) throws IOException {
        log.debug("Reading file: {}", file.length());
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
            while (channel.read(buffer) > 0) {
                buffer.flip(); // 准备读取缓冲区中的数据
                // 处理读取到的数据块
                byte[] data = Arrays.copyOfRange(buffer.array(), 0, buffer.remaining());
                log.debug("Read: {}", data.length);
                consumer.accept(data);
                buffer.clear(); // 清空缓冲区以便下次读取
            }
        }
    }

}
