package com.github.mbmll.concept;


import lombok.Data;
import org.junit.Test;

import java.util.Map;

/**
 * @Author xlc
 * @Description
 * @Date 2023/7/25 18:13
 */

class TypeMapperTest {

    @Test
    void getMapper() {
        BooleanMapper mapper = new BooleanMapper();

    }

    /**
     * @Author xlc
     * @Description
     * @Date 2023/7/25 18:17
     */
    @Data
    public static class BooleanMapperDemo {
        private Boolean enableFlg;
    }

    /**
     * @Author xlc
     * @Description
     * @Date 2023/7/25 18:17
     */

    public static class BooleanMapper implements Mapper<String, Boolean, BooleanMapperDemo, Map<String, Object>> {
        @Override
        public Boolean parse(Map<String, Object> map) {
            return "是".equals(String.valueOf(map.get("enableFlg")));
        }

        @Override
        public String generate(BooleanMapperDemo demo) {
            return demo.getEnableFlg() ? "是" : "否";
        }
    }
}