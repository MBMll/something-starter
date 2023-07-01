package org.github.mbmll.starters.utils.common;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * if you want to save order of list on frontend into database, take it to clean up the order of data.
 *
 * @Author xlc
 * @Description
 * @Date 2023/6/3 0:59
 */

public abstract class EntitySorter<E, K> {

    private final int start;
    private final List<E> list;
    private HashMap<K, Integer> map;

    public EntitySorter(int start, List<E> list) {
        this.start = start;
        this.list = list;
    }

    public List<E> move(E moved, int offset) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        // @notice if it cannot be complied successfully on idea, set 'target byte version' on settings of idea.
        var current = list.stream()
                .filter(e -> Objects.equals(getKey(moved), getKey(e)))
                .findFirst().orElseGet(() -> {
                    list.add(moved);
                    return moved;
                });
        int currentPosition = list.indexOf(current);
        int toPosition = currentPosition + offset;
        // limit
        toPosition = Math.max(toPosition, 0);
        toPosition = Math.min(toPosition, list.size() - 1);
        // move
        list.remove(currentPosition);
        list.add(toPosition, current);
        // rearrange
        map = new HashMap<>();
        for (var i = 0; i < list.size(); i++) {
            map.put(getKey(list.get(i)), getValue(list.get(i)));
            setValue(list.get(i), i + start);
        }
        return list.stream().filter(e -> !Objects.equals(getValue(e), map.get(getKey(e)))).collect(Collectors.toList());
    }

    public abstract K getKey(E e);

    public abstract Integer getValue(E e);

    public abstract void setValue(E e, int v);
}
