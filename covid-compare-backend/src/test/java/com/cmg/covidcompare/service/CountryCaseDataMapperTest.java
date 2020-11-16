package com.cmg.covidcompare.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryCovidData;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryCaseDataMapperTest {

    private CountryCaseDataMapper mapper = new CountryCaseDataMapper();

    private final static String COUNTRY_CODE = "AUT";
    private final static int CASES_TODAY = 1000;
    private final static int CASES_PER_HUNDRED_THOUSAND = 10;
    private final static int DEATHS = 5;
    private final static String DATE_STR = "2020-01-13";
    private final static String DATE_STR_TWO = "2020-01-13";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static LocalDate DATE = LocalDate.parse(DATE_STR, DATE_TIME_FORMATTER);
    private final static LocalDate DATE_TWO = LocalDate.parse(DATE_STR_TWO, DATE_TIME_FORMATTER);

    @Mock
    private CountryData countryData;

    @Mock
    private CountryData countryDataTwo;

    private Set<CountryData> countryStatusSet;

    @BeforeEach
    void beforeEach() {
        // Return positive integer then negative integer
        when(countryData.getCasesToday()).thenReturn(CASES_TODAY).thenReturn((-CASES_TODAY));
        when(countryData.getCasesPerHundredThousand()).thenReturn(CASES_PER_HUNDRED_THOUSAND).thenReturn((-CASES_PER_HUNDRED_THOUSAND));
        when(countryData.getDeathsToday()).thenReturn(DEATHS).thenReturn((-DEATHS));
        when(countryData.getDate()).thenReturn(DATE);

        when(countryDataTwo.getCasesToday()).thenReturn((-CASES_TODAY));
        when(countryDataTwo.getCasesPerHundredThousand()).thenReturn((-CASES_PER_HUNDRED_THOUSAND));
        when(countryDataTwo.getDeathsToday()).thenReturn((-DEATHS));
        when(countryDataTwo.getDate()).thenReturn(DATE_TWO);
        when(countryDataTwo.compareTo(any())).thenReturn(-1);
        countryStatusSet = new TreeSet<>(Arrays.asList(countryDataTwo, countryData ));
    }


    void mapToCountryCaseData() {

        CountryCovidData actual = mapper.mapToCountryCaseData(COUNTRY_CODE, countryStatusSet);

        assertThat(actual).isNotNull();
        assertThat(actual.getCases().length).isEqualTo(countryStatusSet.size());

        assertThat(actual.getCases()[0].getName()).isEqualTo(DATE_STR);
        assertThat(actual.getCases()[0].getValue()).isEqualTo(CASES_TODAY);

        assertThat(actual.getCasesPerHundredThousand()[0].getName()).isEqualTo(DATE_STR);
        assertThat(actual.getCasesPerHundredThousand()[0].getValue()).isEqualTo(CASES_PER_HUNDRED_THOUSAND);

        assertThat(actual.getDeaths()[0].getName()).isEqualTo(DATE_STR);
        assertThat(actual.getDeaths()[0].getValue()).isEqualTo(DEATHS);
    }


    void mapToCountryCaseData_givenMinusFigures_expectZero() {
        CountryCovidData actual = mapper.mapToCountryCaseData(COUNTRY_CODE, countryStatusSet);

        assertThat(actual).isNotNull();
        assertThat(actual.getCases().length).isEqualTo(countryStatusSet.size());

        assertThat(actual.getCases()[0].getName()).isEqualTo(DATE_STR_TWO);
        assertThat(actual.getCases()[0].getValue()).isEqualTo(0);

        assertThat(actual.getCasesPerHundredThousand()[0].getName()).isEqualTo(DATE_STR_TWO);
        assertThat(actual.getCasesPerHundredThousand()[0].getValue()).isEqualTo(0);

        assertThat(actual.getDeaths()[0].getName()).isEqualTo(DATE_STR);
        assertThat(actual.getDeaths()[0].getValue()).isEqualTo(0);
    }

}
