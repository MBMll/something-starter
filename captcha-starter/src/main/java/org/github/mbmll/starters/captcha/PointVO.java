package org.github.mbmll.starters.captcha;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by raodeming on 2020/5/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointVO {
    private Integer x;
    private Integer y;
    private String secretKey;

    public static PointVO parse(String v) {
        PointVO vo = new PointVO();
        String[] split = v.split(",");
        vo.setX(Integer.valueOf(split[0]));
        vo.setY(Integer.valueOf(split[1]));
        vo.setSecretKey(split[2]);
        return vo;
    }

    @Override
    public String toString() {
        return Stream.of(x, y, secretKey).map(Objects::toString).collect(Collectors.joining(","));
    }
}
