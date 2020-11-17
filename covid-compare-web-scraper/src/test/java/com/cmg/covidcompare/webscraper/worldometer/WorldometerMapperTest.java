package com.cmg.covidcompare.webscraper.worldometer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import com.gargoylesoftware.htmlunit.html.DomElement;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

@ExtendWith(MockitoExtension.class)
class WorldometerMapperTest {

    private WorldometerMapper mapper = new WorldometerMapper();

    @Mock
    private DomElement element;

    private final static String COUNTRY_NAME = "Switzerland";
    private final static LocalDate DATE = LocalDate.now();

    @Test
    void mapCountryDayData_givenCases_expectCasesReturned() throws Exception {

        when(element.asText()).thenReturn(getNewsElementWithCasesAsText());

        CountryDayData actual = mapper.mapCountryDayData(element, COUNTRY_NAME, DATE);

        assertThat(actual).isNotNull();
        assertThat(actual.getCases()).isGreaterThan(0);
        assertThat(actual.getDeaths()).isGreaterThan(0);
    }

    @Test
    void mapCountryDayData_givenNoCases_expectDeathsReturned() throws Exception {
        when(element.asText()).thenReturn(getNewsElementWithoutCasesAsText());

        CountryDayData actual = mapper.mapCountryDayData(element, COUNTRY_NAME, DATE);

        assertThat(actual).isNotNull();
        assertThat(actual.getCases()).isEqualTo(0);
        assertThat(actual.getDeaths()).isGreaterThan(0);
    }

    private String getNewsElementWithCasesAsText() throws Exception {
        return Files.readString(new ClassPathResource("element_with_cases.html").getFile().toPath());
    }

    private String getNewsElementWithoutCasesAsText() throws Exception {
        return Files.readString(new ClassPathResource("element_without_cases.html").getFile().toPath());
    }

}
