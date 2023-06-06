package org.github.mbmll.starters.utils;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/5 9:23
 */

class EntitySorterTest {

    @Test
    void move() {

        List<Entity> list = new ArrayList<>();
        MockConfig config = MockConfig.newInstance();
        for (int i = 0; i < 10; i++) {
            Entity entity = JMockData.mock(Entity.class, config);
            list.add(entity);
        }
        EntitySorter<Entity, String> sorter = new EntitySorter<Entity, String>(0, list) {
            @Override
            public String getKey(Entity entity) {
                return entity.id;
            }

            @Override
            public Integer getValue(Entity entity) {
                return entity.serialNum;
            }

            @Override
            public void setValue(Entity entity, int v) {
                entity.serialNum = v;
            }
        };
        list.get(2).serialNum = null;
        System.out.println(list);
        System.out.println(sorter.move(list.get(0), 4));
    }

    public static class Entity {

        private String id;
        private Integer serialNum;

        @Override
        public String toString() {
            return "Entity{" +
                    "id='" + id + '\'' +
                    ", serialNum=" + serialNum +
                    '}';
        }
    }
}
