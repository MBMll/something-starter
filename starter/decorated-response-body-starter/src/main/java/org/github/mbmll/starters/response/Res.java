package org.github.mbmll.starters.response;

import lombok.Builder;
import lombok.Data;

/**
 * @Author xlc
 * @Description
 * @Date 2023/1/30 上午 10:38
 */
@Data
@Builder
public class Res<T> {
    private T data;
}
