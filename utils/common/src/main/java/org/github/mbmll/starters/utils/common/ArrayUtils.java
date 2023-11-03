package org.github.mbmll.starters.utils.common;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/3 12:11
 */

public class ArrayUtils {

    /**
     * spin elements in assigned range.
     *  such as : [1, 2, 3, 4, 5, 6, 7, 8, 9] => spin(arr, 0, 5, 4) => [5, 1, 2, 3, 4, 6, 7, 8, 9]
     * @param arr    data
     * @param start  start index
     * @param size   size of change
     * @param offset offset of change
     * @param <T>    type
     */
    public static <T> void spin(T[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        T first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(short[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        short first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(char[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        char first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(long[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        long first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(byte[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        byte first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(boolean[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        boolean first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(double[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        double first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(float[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        float first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }

    /**
     * @param arr
     * @param start
     * @param size
     * @param offset
     */
    public static void spin(int[] arr, int start, int size, int offset) {
        if (start < 0 || start >= arr.length)
            return;
        int first = arr[start];
        int prev = 0;
        int next = 0;
        int range = Math.min(arr.length - start, size);
        if (range > 1) {
            for (int i = 0; i < range - 1; i++) {
                next = (prev + offset) % range;
                arr[start + prev] = arr[start + next];
                prev = next;
            }
            arr[start + prev] = first;
        }
    }
}
