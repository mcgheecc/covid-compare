package com.cmg.covidcompare.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

    public class LocalDateUtil {

    private static ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }
}
