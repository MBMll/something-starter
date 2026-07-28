package org.github.mbmll.starters.utils.common.parallel;


import com.github.mbmll.concept.exception.ThrowingFunction;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @Author xlc
 * @Description
 * @Date 2026/7/28 23:59
 */

public class ParallelIterable<T> implements Iterable<T>, Closeable {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Config config;
    private final BlockingQueue<T> queue;
    private final ExecutorService pool;

    /**
     * @param config
     * @param iterator
     * @param consumer
     */
    public ParallelIterable(Config config, Iterator<T> iterator, ThrowingFunction<T, T, Exception> consumer) {
        this.config = config;
        queue = new LinkedBlockingQueue<>(config.bufferSize);
        pool = createThreadPool();
        new Thread(() -> {
            try (this) {
                while (iterator.hasNext()) {
                    T next = iterator.next();
                    pool.submit(() -> {
                        try {
                            queue.put(consumer.apply(next));
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    });
                    if (isClosed()) {
                        try {
                            close(pool);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }
                try {
                    close(pool);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * @return
     */
    private boolean isClosed() {
        return closed.get();
    }

    /**
     * @param pool
     *
     * @throws InterruptedException
     */
    private void close(ExecutorService pool) throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(config.timeout, config.timeUnit);
    }

    /**
     *
     */
    @Override
    public void close() {
        closed.set(true);
    }

    /**
     * @return
     */
    private ExecutorService createThreadPool() {
        return new ThreadPoolExecutor(config.parallelism, config.parallelism,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(config.bufferSize),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private T next;

            /**
             * @return
             */
            @Override
            public boolean hasNext() {
                if (next == null) {
                    try {
                        next = tryNext();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return next != null;
            }

            /**
             * @return
             *
             * @throws InterruptedException
             */
            private T tryNext() throws InterruptedException {
                while (true) {
                    T next = null;
                    try {
                        next = queue.poll(10, TimeUnit.MILLISECONDS);
                        if (next != null) {
                            return next;
                        }
                    } finally {
                        // if pool is terminated,
                        if (pool.isTerminated() && queue.isEmpty()) {
                            System.out.println("pool is terminated: " + pool.isTerminated());
                            System.out.println("queue is empty: " + queue.isEmpty());
                            return next;
                        }
                    }
                }
            }

            /**
             * @return
             */
            @Override
            public T next() {
                T current = next;
                next = null;
                return current;
            }
        };
    }

    /**
     * @param action The action to be performed for each element
     */
    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    /**
     * @return
     */
    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    /**
     *
     */
    public static class Config {
        private int parallelism = 4;
        private int bufferSize = 500;
        private long timeout = 1;
        private TimeUnit timeUnit = TimeUnit.HOURS;
    }
}
