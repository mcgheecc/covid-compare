package com.cmg.covidcompare.webscraper;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestConstants {

    public final static String COUNTRY_NAME = "austria";
    public final static int DEATHS = 0;
    public final static int CASES = 101;
    public final static String DATE = "2020-09-01";
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final static LocalDate TODAY = LocalDate.parse(DATE, DATE_FORMATTER);
}
