package org.github.mbmll.starters.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * @Author xlc
 * @Description
 * @Date 2023/7/5 16:11
 */

public class LocalDateTest {
    @Test
    public void t() {
        LocalDateTime dateTime = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
        System.out.println(dateTime.truncatedTo(ChronoUnit.HOURS));
        System.out.println(dateTime.with(ChronoField.HOUR_OF_DAY, dateTime.getHour()));
    }
}
