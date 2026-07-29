package org.github.mbmll.starters.utils.common.parallel;

import com.github.jsonzou.jmockdata.util.RandomUtils;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.github.mbmll.starters.utils.common.parallel.ParallelIterable.*;

/**
 * @Author xlc
 * @Description
 * @Date 2026/7/29 01:21
 */

public class ParallelIterableTest extends TestCase {

    /**
     *
     */
    public void testParallelIterable() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            list.add(i + " : " + UUID.randomUUID());
        }
        Config config = new Config();
        Iterator<String> iterator = new ParallelIterable<>(config, list.iterator(), (e) -> {
            Thread.sleep(100L);
            return e;
        }).iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     *
     */
    public void testParallelIterableForeach() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            list.add(i + " : " + UUID.randomUUID());
        }
        Config config = new Config();
        ParallelIterable<String> iterable = new ParallelIterable<>(config, list.iterator(), (String e) -> {
            Thread.sleep(100L);
            return e;
        });
        iterable.forEach(System.out::println);
    }
}