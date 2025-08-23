package org.github.mbmll.starters.utils.common.concurrent;


import com.github.mbmll.concept.exception.ThrowingConsumer;
import com.github.mbmll.concept.exception.ThrowingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 异步任务构建器
 */
public class AsyncBuilder {
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(AsyncBuilder.class);
    /**
     *
     */
    private final List<FutureTask<Void>> tasks = new ArrayList<>();

    /**
     * @return
     */
    public static AsyncBuilder builder() {
        return new AsyncBuilder();
    }

    /**
     * 开启一个异步回调任务
     *
     * @return
     *
     */
    private static <T> FutureTask<T> runAsync(Callable<T> callable) {
        FutureTask<T> task = new FutureTask<>(callable);
        new Thread(task).start();
        return task;
    }

    public void build() {
        for (FutureTask<Void> task : tasks) {
            try {
                task.get();
            } catch (Exception e) {
                logger.error("async error", e);
            }
        }
    }

    /**
     * @param intput
     * @param consumer
     * @param <T>
     * @param <E>
     */
    public <T, E extends Throwable> void end(BlockingChannel<T> intput,
                                             ThrowingConsumer<T, E> consumer) {
        tasks.add(runAsync(() -> {
            try {
                T target;
                while ((target = intput.poll()) != null) {
                    consumer.accept(target);
                }
            } catch (Throwable e) {
                logger.error("async error", e);
                intput.close();
            }
            return null;
        }));
    }

    /**
     * @param consumer
     * @param <E>
     *
     * @return
     */
    public <T, E extends Throwable> BlockingChannel<T> start(ThrowingConsumer<BlockingChannel<T>, E> consumer) {
        BlockingChannel<T> channel = new BlockingChannel<>();
        tasks.add(runAsync(() -> {
            try {
                consumer.accept(channel);
            } catch (Throwable e) {
                logger.error("async error", e);
            } finally {
                channel.close();
            }
            return null;
        }));
        return channel;
    }

    /**
     * @param intput
     * @param callback
     * @param <T>
     * @param <R>
     * @param <E>
     *
     * @return
     */
    public <T, R, E extends Throwable> BlockingChannel<T> transform(BlockingChannel<T> intput,
                                                                    ThrowingFunction<T, R, E> callback) {
        BlockingChannel<T> output = new BlockingChannel<>();
        tasks.add(runAsync(() -> {
            try {
                T target;
                while ((target = intput.poll()) != null) {
                    callback.apply(target);
                }
            } catch (Throwable e) {
                logger.error("async error", e);
                intput.close();
            } finally {
                output.close();
            }
            return null;
        }));
        return output;
    }
}
