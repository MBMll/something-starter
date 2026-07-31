package com.github.mbmll.concept.fields;

import java.util.Date;

/**
 * 物料实体基础信息
 *
 * @param <I>
 * @param <D>
 * @param <B>
 */
public interface MaterialEssential<I, D, B>
    extends Essential<I, D, B>, Name {
    /**
     * @param e
     * @param userId
     */
    static void create(MaterialEssential e,
                       String userId) {
        e.setDeleted(false);
//        e.setCreateId(userId);
        e.setCreateTime(new Date());
    }

    static void update(MaterialEssential e,
                       String userId) {
//        e.setUpdateId(userId);
        e.setUpdateTime(new Date());
    }

    static void save(MaterialEssential e,
                     String userId) {
        if (e.getId() == null) {
            create(e,
                   userId);
        }
        update(e,
               userId);
    }
}
