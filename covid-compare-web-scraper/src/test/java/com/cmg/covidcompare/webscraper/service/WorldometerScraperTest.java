package com.cmg.covidcompare.webscraper.service;

import static com.cmg.covidcompare.webscraper.TestConstants.CASES;
import static com.cmg.covidcompare.webscraper.TestConstants.COUNTRY_NAME;
import static com.cmg.covidcompare.webscraper.TestConstants.DEATHS;
import static com.cmg.covidcompare.webscraper.TestConstants.TODAY;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import com.cmg.covidcompare.webscraper.worldometer.WorldometerMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldometerScraperTest {

    private  WorldometerScraper scraper;
    @Mock
    private WebClient mockClient;
    @Mock
    private WorldometerMapper mockMapper;
    @Mock
    private HtmlPage htmlPage;
    @Mock
    private DomElement domElement;
    @Mock
    private WebClientOptions webClientOptions;

    private final static CountryDayData DATA = new CountryDayData(COUNTRY_NAME, CASES, 0, DEATHS, TODAY, LocalDateTime.now());

    @BeforeEach
    void beforeEach() {
        scraper = new WorldometerScraper(mockClient, mockMapper);
        scraper.initMap();
    }

    @Test
    void getStats() throws Exception {
        when(htmlPage.getElementById(any(String.class))).thenReturn(domElement);
        when(mockMapper.mapCountryDayData(any(DomElement.class), any(String.class), any(LocalDate.class)))
            .thenReturn(DATA);
        when(mockClient.getPage(any(String.class))).thenReturn(htmlPage);
        when(mockClient.getOptions()).thenReturn(webClientOptions);

        Optional<CountryDayData> optional = scraper.getStats("austria", LocalDate.now());

        assertThat(optional.isPresent()).isTrue();
        CountryDayData data = optional.get();
        assertThat(data.getDate()).isEqualTo(DATA.getDate());
        assertThat(data.getName()).isEqualTo(DATA.getName());
        assertThat(data.getCases()).isEqualTo(DATA.getCases());
        assertThat(data.getDeaths()).isEqualTo(DATA.getDeaths());
    }

    @Test
    void getStats_givenNullElement_expectOptionalIsEmpty() throws Exception {
        when(mockClient.getOptions()).thenReturn(webClientOptions);
        when(mockClient.getPage(any(String.class))).thenReturn(htmlPage);
        when(htmlPage.getElementById(any(String.class))).thenReturn(null);
        Optional<CountryDayData> optional = scraper.getStats("austria", LocalDate.now());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    void getStats_givenIOException_expectOptionIsEmpty() throws Exception {
        when(mockClient.getOptions()).thenReturn(webClientOptions);
        when(mockClient.getPage(any(String.class))).thenThrow(new IOException());
        Optional<CountryDayData> optional = scraper.getStats("austria", LocalDate.now());
        assertThat(optional.isEmpty()).isTrue();
    }
}
