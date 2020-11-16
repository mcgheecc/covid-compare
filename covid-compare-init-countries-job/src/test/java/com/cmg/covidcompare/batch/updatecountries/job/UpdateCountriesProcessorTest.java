package com.cmg.covidcompare.batch.updatecountries.job;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.client.WorldometerWebScraperFeignClient;
import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCountriesProcessorTest {

    @Mock
    private WorldometerWebScraperFeignClient client;

    @InjectMocks
    private UpdateCountriesProcessor processor = new UpdateCountriesProcessor();

    private final static String COUNTRY_CODE = "AUT";
    private final static  String NAME = "Austria";
    private final static LocalDate DATE = LocalDate.now();

    private final static int POPULATION = 9000000;

    @Test
    void process_expectPopulationReturned() {
        when(client.getWorldometerCountryPopulation(any(String.class))).thenReturn(POPULATION);
        CountryDto countryDto = new CountryDto(COUNTRY_CODE, NAME, DATE);
        Country actual = processor.process(countryDto);

        assertThat(actual).isNotNull();
        assertThat(actual.getPopulation()).isEqualTo(POPULATION);
    }

    @Test
    void process_givenZeroPopulation_expectMissingPopulationException() {
        when(client.getWorldometerCountryPopulation(any(String.class))).thenReturn(0);

        CountryDto countryDto = new CountryDto(COUNTRY_CODE, NAME, DATE);

        assertThrows(MissingPopulationException.class, () -> processor.process(countryDto));
    }
}
