package org.github.mbmll.starters.utils.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author xlc
 * @Description
 * @Date 2023/2/9 下午 03:58
 */

public class BigDecimalUtils {

    /**
     * big decimal multiply.
     *
     * @param a a
     * @param b b
     *
     * @return result
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return multiply(a, b, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    /**
     * big decimal multiply.
     *
     * @param a         a
     * @param b         b
     * @param aDefaultV a default value
     * @param bDefaultV b default value
     *
     * @return result
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b, BigDecimal aDefaultV, BigDecimal bDefaultV) {
        if (a == null) {
            a = aDefaultV;
        }
        if (b == null) {
            b = bDefaultV;
        }
        return a.multiply(b);
    }

    /**
     * big decimal divide.
     *
     * @param a     divided
     * @param b     divisor
     * @param round round mode
     *
     * @return a/b
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, RoundingMode round) {
        return divide(a, b, round, null);
    }

    /**
     * big decimal divide.
     *
     * @param a     divided
     * @param b     divisor
     * @param scale scale
     * @param round round mode
     *
     * @return a/b
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale, RoundingMode round) {
        return divide(a, b, scale, round, null);
    }

    /**
     * big decimal divide.
     *
     * @param a        divided
     * @param b        divisor
     * @param round    round mode
     * @param defaultV default value
     *
     * @return a/b
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, RoundingMode round, BigDecimal defaultV) {
        if (a == null || b == null || BigDecimal.ZERO.equals(b)) {
            return defaultV;
        }
        return a.divide(b, Math.max(a.scale(), b.scale()), round);
    }

    /**
     * big decimal divide.
     *
     * @param a     divided
     * @param b     divisor
     * @param scale scale
     * @param round round mode
     *
     * @return a/b
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale, RoundingMode round, BigDecimal defaultV) {
        if (a == null || b == null || BigDecimal.ZERO.equals(b)) {
            return defaultV;
        }
        return a.divide(b, scale, round);
    }
}
