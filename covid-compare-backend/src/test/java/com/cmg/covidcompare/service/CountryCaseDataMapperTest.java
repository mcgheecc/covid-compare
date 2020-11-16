package com.cmg.covidcompare.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryCovidData;
import com.cmg.covidcompare.domain.NameValue;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryCaseDataMapperTest {

    private CountryCaseDataMapper mapper = new CountryCaseDataMapper();

    private final static String COUNTRY_CODE = "AUT";
    private final static int CASES_TODAY = 1000;
    private final static int CASES_PER_HUNDRED_THOUSAND = 10;
    private final static int DEATHS = 5;
    private final static String DATE_STR = "2020-01-13";
    private final static String DATE_STR_TWO = "2020-01-14";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static LocalDate DATE = LocalDate.parse(DATE_STR, DATE_TIME_FORMATTER);
    private final static LocalDate DATE_TWO = LocalDate.parse(DATE_STR_TWO, DATE_TIME_FORMATTER);
    private static CountryData COUNTRY_DATA = new CountryData();
    private static Country COUNTRY = new Country();
    private static CountryData COUNTRY_DATA_TWO = new CountryData();

    private static Set<CountryData> COUNTRY_STATUS_SET;

    @BeforeAll
    static void beforeEach() {
        // Return positive integer then negative integer
        COUNTRY.setName("Austria");
        COUNTRY.setCountryCode("AUT");
        COUNTRY_DATA.setCountry(COUNTRY);
        COUNTRY_DATA.setCasesToday(CASES_TODAY);
        COUNTRY_DATA.setCasesPerHundredThousand(CASES_PER_HUNDRED_THOUSAND);
        COUNTRY_DATA.setDeathsToday(DEATHS);
        COUNTRY_DATA.setDate(DATE);

        COUNTRY_DATA_TWO.setCountry(COUNTRY);
        COUNTRY_DATA_TWO.setCasesToday(-CASES_TODAY);
        COUNTRY_DATA_TWO.setCasesPerHundredThousand(-CASES_PER_HUNDRED_THOUSAND);
        COUNTRY_DATA_TWO.setDeathsToday(-DEATHS);
        COUNTRY_DATA_TWO.setDate(DATE_TWO);

        COUNTRY_STATUS_SET = new TreeSet<>(Arrays.asList(COUNTRY_DATA, COUNTRY_DATA_TWO ));
    }

    @Test
    void mapToCountryCaseData() {

        CountryCovidData actual = mapper.mapToCountryCaseData(COUNTRY_CODE, COUNTRY_STATUS_SET);

        assertThat(actual).isNotNull();
        assertThat(actual.getCases()).hasSize(COUNTRY_STATUS_SET.size());

        NameValue expected = new NameValue(DATE_STR, CASES_TODAY);
        assertNameValueIsPresent(expected, actual.getCases());

        expected = new NameValue(DATE_STR, CASES_PER_HUNDRED_THOUSAND);
        assertNameValueIsPresent(expected, actual.getCasesPerHundredThousand());

        expected = new NameValue(DATE_STR, DEATHS);
        assertNameValueIsPresent(expected, actual.getDeaths());

    }

    @Test
    void mapToCountryCaseData_givenMinusFigures_expectZero() {
        CountryCovidData actual = mapper.mapToCountryCaseData(COUNTRY_CODE, COUNTRY_STATUS_SET);

        assertThat(actual).isNotNull();
        assertThat(actual.getCases()).hasSize(COUNTRY_STATUS_SET.size());

        NameValue expected = new NameValue(DATE_STR_TWO, 0);
        assertNameValueIsPresent(expected, actual.getCases());
        assertNameValueIsPresent(expected, actual.getCasesPerHundredThousand());
        assertNameValueIsPresent(expected, actual.getDeaths());
    }

    private void assertNameValueIsPresent(NameValue expected, NameValue[] list) {
        assertThat(Arrays.asList(list)).contains(expected);
    }

}
