package org.github.mbmll.starters.utils.common;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/3 12:27
 */

public class ArrayUtilsTest extends TestCase {

    public void testSpin() {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                for (int k = 0; k < 30; k++) {
                    Object[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                    ArrayUtils.spin(arr, i, j, k);
                    if (!Arrays.equals(arr, new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9})) {
                        System.out.println(i);
                        System.out.println(j);
                        System.out.println(k);
                        System.out.println(Arrays.asList(arr));
                    }
                }
            }
        }

    }
}