package com.cmg.covidcompare.util;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class LocalDateUtilTest {

    private final static String PATTERN = "yyyy-MM-dd";
    private final static String DATE_STR = "2020-01-01";
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    @Test
    void toLocalDate() throws Exception {
        LocalDate expected = LocalDate.parse(DATE_STR, FORMATTER);
        Date date = new SimpleDateFormat(PATTERN).parse(DATE_STR);

        LocalDate actual = LocalDateUtil.toLocalDate(date);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toDate() throws Exception {
        Date expected  = new SimpleDateFormat(PATTERN).parse(DATE_STR);
        LocalDate localDate = LocalDate.parse(DATE_STR, FORMATTER);

        Date actual = LocalDateUtil.toDate(localDate);

        assertThat(actual).isEqualTo(expected);
    }
}
