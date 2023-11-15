package org.github.mbmll.starters.captcha;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by raodeming on 2020/5/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointVO implements Serializable {
    private Integer x;
    private Integer y;
    private String secretKey;
}
