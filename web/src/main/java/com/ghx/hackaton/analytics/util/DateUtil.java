package com.ghx.hackaton.analytics.util;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static int year(Date date) {
        return year(date.getTime());
    }

    public static int year(long timeMillis) {
        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTimeInMillis(timeMillis);

        return calendar.get(Calendar.YEAR);
    }

    public static int month(long timeMillis) {
        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTimeInMillis(timeMillis);

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int month(Date date) {
        return month(date.getTime());
    }

    public static int dayOfMonth(long timeMillis) {
        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTimeInMillis(timeMillis);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int dayOfMonth(Date date) {
        return dayOfMonth(date.getTime());
    }

    public static int hourOfDay(long timeMillis) {
        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTimeInMillis(timeMillis);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int hourOfDay(Date date) {
        return hourOfDay(date.getTime());
    }

    public static int minute(long timeMillis) {
        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTimeInMillis(timeMillis);

        return calendar.get(Calendar.MINUTE);
    }

    public static int minute(Date date) {
        return minute(date.getTime());
    }

    public static Date truncateToHour(Date date) {
        return DateUtils.truncate(date, Calendar.HOUR);
    }

    public static long timestamp(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(UTC);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return DateUtils.truncate(calendar, Calendar.DATE).getTimeInMillis();
    }
}
