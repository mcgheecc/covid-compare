package com.cmg.covidcompare.batch.updatecountries.job;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.client.CovidApiClient;
import com.cmg.covidcompare.domain.CountryDto;
import com.cmg.covidcompare.domain.CountryResponse;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCountriesReaderTest {

    @Mock
    private CovidApiClient covidApiClient;

    @Mock
    private CountryResponse countryResponse;

    @InjectMocks
    private UpdateCountriesReader reader;

    private final static String COUNTRY_CODE = "AUT";
    private final static  String NAME = "Austria";
    private final static LocalDate DATE = LocalDate.now();

    @Test
    void read() {
        when(covidApiClient.getCountries()).thenReturn(countryResponse);
        CountryDto expected = new CountryDto(COUNTRY_CODE, NAME, DATE);
        when(countryResponse.getCountries()).thenReturn(Collections.singletonList(expected));

        CountryDto actual = reader.read();

        assertThat(actual).isEqualTo(expected);
    }
}
