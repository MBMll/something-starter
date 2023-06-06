package org.github.mbmll.starters.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author xlc
 * @Description
 * @Date 2023/6/5 13:21
 */

public class SerializeUtil {

    public static <V> V toObject(String target, Class<V> clazz) throws JsonProcessingException {
        return getMapper().readValue(target, clazz);
    }

    public static <V> String fromObject(V target) throws JsonProcessingException {
        return getMapper().writeValueAsString(target);
    }

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        return mapper;
    }
}
