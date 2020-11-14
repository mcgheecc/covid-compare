package com.cmg.covidcompare.webscraper.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import com.cmg.covidcompare.webscraper.model.CountryDayData;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorldometerScraperIntegrationTest {

    @Autowired
    private WorldometerScraper scraper;

    @Value("${urls.worldometer.country.coronavirus}")
    private String worldometersCoronavirusUrl;

    @Value("${urls.worldometer.country.population}")
    private String worldometersPopulationUrl;

    @Test
    void test_getStats_given_finland_expect_results_returned() {
        testCountryStatsAreReturned("finland");
    }

    @Test
    void test_getStats_given_austria_expect_results_returned() {
        testCountryStatsAreReturned("austria");
    }

    private void testCountryStatsAreReturned(String countryName) {
        CountryDayData countryDayData = getStatsForYesterdayOrDayBefore(countryName);
        assertEquals(countryDayData.getName(), countryName);
    }

    private CountryDayData getStatsForYesterdayOrDayBefore(String countryName) {
        LocalDate date = LocalDate.now().minusDays(1);

        Optional<CountryDayData> data = scraper.getStats(countryName, date);

        if (data.isEmpty()) { // No data for yesterday. Let's try again with 2 days ago.
            date = LocalDate.now().minusDays(2);
            data = scraper.getStats(countryName, date);
        }
        return data.orElseThrow();
    }

    @Test
    void getCountryPopulation_finland() {
        Optional<Integer> optional = scraper.getPopulation("finland");
        assertTrue(optional.isPresent());
        Integer actual = optional.get();
        assertThat(actual).isGreaterThan(1000000);
    }

    @Test
    void getCountryPopulation_austria() {
        Optional<Integer> optional = scraper.getPopulation("austria");
        assertTrue(optional.isPresent());
        Integer actual = optional.get();
        assertThat(actual).isGreaterThan(1000000);
    }
}
