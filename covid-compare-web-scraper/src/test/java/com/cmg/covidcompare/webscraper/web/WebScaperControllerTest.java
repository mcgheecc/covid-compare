package com.cmg.covidcompare.webscraper.web;

import static com.cmg.covidcompare.webscraper.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import com.cmg.covidcompare.webscraper.service.WorldometerScraper;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebScaperControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private WorldometerScraper worldometerScraper;

    private final static CountryDayData DATA = new CountryDayData(COUNTRY_NAME, CASES, 0, DEATHS, TODAY, LocalDateTime.now());

    private final static Integer EXPECTED_POPULATION = 1000000;

    @BeforeEach
    void beforeEach() {
        when(worldometerScraper.getStats(any(String.class), any(LocalDate.class))).thenReturn(Optional.of(DATA));
        when(worldometerScraper.getPopulation(any(String.class))).thenReturn(Optional.of(EXPECTED_POPULATION));
    }

    @Test
    void getWorldometerCountryDayData() throws Exception {
        String url = String.format("http://localhost:%s/api/v1/scraper/worldometer/%s/%s", port, COUNTRY_NAME, DATE);

        ResponseEntity<CountryDayData> response = restTemplate.getForEntity(
            new URL(url).toString(), CountryDayData.class);

        CountryDayData data = response.getBody();

        assertThat(data).isNotNull();
        assertThat(data.getName()).isEqualTo(DATA.getName());
        assertThat(data.getCases()).isEqualTo(DATA.getCases());
        assertThat(data.getDeaths()).isEqualTo(DATA.getDeaths());
        assertThat(data.getDate()).isEqualTo(DATA.getDate());
    }

    @Test
    void getWorldometerPopulation() throws Exception {
        String url = String.format("http://localhost:%s/api/v1/scraper/worldometer/population/%s", port, COUNTRY_NAME);
        ResponseEntity<Integer> response = restTemplate.getForEntity(
            new URL(url).toString(), Integer.class);

        Integer population = response.getBody();
        assertThat(population).isEqualTo(EXPECTED_POPULATION);
    }
}
