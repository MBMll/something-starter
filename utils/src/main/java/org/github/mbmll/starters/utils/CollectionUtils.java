package org.github.mbmll.starters.utils;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * collection utils.
 *
 * @Author xlc
 * @Description
 * @Date 2023/2/22 22:52
 */
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> target) {
        return target == null || target.isEmpty();
    }

    public static <T> List<T> toTree(List<T> sources, Comparator<T> comparator, Collector<T> collector) {
        return getChildren(sources, null, comparator, collector);
    }

    private static <T> List<T> getChildren(List<T> sources, T parent, Comparator<T> comparator,
        Collector<T> collector) {
        if (!isEmpty(sources)) {
            Map<Boolean, List<T>> collect = sources.stream()
                .collect(Collectors.groupingBy(child -> comparator.compare(parent, child)));
            List<T> childs = collect.get(true);
            if (!isEmpty(childs)) {
                for (T child : childs) {
                    collector.putAll(child, getChildren(collect.get(false), child, comparator, collector));
                }
                return childs;
            }
        }
        return Collections.emptyList();
    }

    /**
     * @param sources
     * @param classifier
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> Map<K, List<T>> groupBy(Iterable<T> sources, Function<T, K> classifier) {
        HashMap<K, List<T>> map = new HashMap<>();
        for (T source : sources) {
            K key = classifier.apply(source);
            map.computeIfAbsent(key, k -> new ArrayList<>());
            map.get(key).add(source);
        }
        return map;
    }

    public static <T, K> List<T> toTree(List<T> sources, Function<T, K> getParent, Function<T, K> getCurrent,
        Collector<T> collector) {

        if (isEmpty(sources)) {
            return sources;
        }
        Map<K, List<T>> groups = groupBy(sources, getParent);
        Map<K, T> map = sources.stream().collect(Collectors.toMap(getCurrent, t -> t));
        List<T> root = groups.remove(null);
        groups.forEach((k, ts) -> collector.putAll(map.get(k), ts));
        return root;
    }

    public interface Comparator<T> {

        boolean compare(T parent, T child);
    }

    public interface Collector<T> {

        void putAll(T parent, List<T> collection);

    }
}
