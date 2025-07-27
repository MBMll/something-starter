package com.github.mbmll.starters.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @Author xlc
 * @Description
 * @Date 2025/7/27 14:22    
 */
@Component
public class FixedBlockingQueueLock {
    private static final String LOCKER_PREFIX = "FIXED_BLOCKING_QUEUE_LOCK:";
    private static final String SEMAPHORE_PREFIX = "SEMAPHORE:";
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 尝试获取固定队列锁, 如果获取失败则返回false, 如果获取成功则返回true。
     * 这种锁机制用于限制同时执行的线程数量，超过限制的线程将会等待。
     *
     * @param lockKey 锁的键值，用于在Redis中唯一标识一个锁。
     * @param waiter  队列长度，即允许等待的线程数量。
     * @return true: 获取锁成功, false: 获取锁失败。
     */
    public boolean tryFixedQueueLock(String lockKey, int waiter) {
        RLock mainLock = redissonClient.getLock(LOCKER_PREFIX + lockKey);
        // 尝试立即获取主锁
        if (mainLock.tryLock()) {
            return true;
        }

        // 检查等待标志（原子操作）
        RSemaphore semaphore = redissonClient.getSemaphore(SEMAPHORE_PREFIX + lockKey);
        // 初始化许可证
        semaphore.trySetPermits(waiter);
        // 设置许可证过期时间
        semaphore.expire(Duration.ofDays(1L));
        // 尝试获取许可证, 如果获取成功则获取锁成功, 否则直接返回  false
        if (semaphore.tryAcquire()) {
            try {
                // 阻塞等待主锁（最多等待30秒）
                mainLock.lock();
                return true;
            } finally {
                semaphore.release();
            }
        }
        return false;
    }
}
