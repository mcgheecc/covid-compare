package com.cmg.covidcompare.web;

import static java.util.Objects.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryCovidData;
import com.cmg.covidcompare.domain.NameValue;
import com.cmg.covidcompare.service.CountryService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CovidCompareControllerTest {
    @LocalServerPort
    private int port;

    @MockBean
    private CountryService countryService;

    @Autowired
    private TestRestTemplate restTemplate;

    private Country finland = new Country("FIN", "Finland", new Date(), 5543667);
    private Country austria = new Country("AUT", "Austria", new Date(), 9023909);

    private final static String COUNTRY_NAME = "Austria";
    private NameValue[] CASES_NAME_VALUES = {new NameValue("2020-10-13", 2930)};
    private NameValue[] CASES_PHT_NAME_VALUES = {new NameValue("2020-10-13", 1)};
    private NameValue[] DEATHS_NAME_VALUES = {new NameValue("2020-10-13", 10)};

   private List<CountryCovidData> countryDataList;

    @Test
    void getCountries() {
        String url = String.format("http://localhost:%s/api/v1/covid/countries", port);
        ResponseEntity<Country[]> entity = restTemplate.getForEntity(url, Country[].class);
        List<Country> countries = Arrays.asList(requireNonNull(entity.getBody()));

        assertThat(countries).contains(finland, austria);
    }

    @Test
    void getCountyData() {
        String url = String.format("http://localhost:%s/api/v1/covid/data/%s/%s/%s",
            port, "FIN,AUT", "2020-10-13", "2020-10-13");
        ResponseEntity<CountryCovidData[]> entity = restTemplate.getForEntity(url, CountryCovidData[].class);
        List<CountryCovidData> actual = Arrays.asList(requireNonNull(entity.getBody()));

        CountryCovidData expected = getCountryCovidData();

        assertThat(actual.get(0)).isEqualTo(countryDataList.get(0));
        assertThat(actual.get(0).getCases()).isEqualTo(expected.getCases());
        assertThat(actual.get(0).getCasesPerHundredThousand()).isEqualTo(expected.getCasesPerHundredThousand());
        assertThat(actual.get(0).getDeaths()).isEqualTo(expected.getDeaths());
    }

    private CountryCovidData getCountryCovidData() {
       CountryCovidData data = new CountryCovidData();
       data.setName(COUNTRY_NAME);
       data.setCases(CASES_NAME_VALUES);
       data.setCasesPerHundredThousand(CASES_PHT_NAME_VALUES);
       data.setDeaths(DEATHS_NAME_VALUES);
       return data;
    }

    @PostConstruct
    private void init() {
        List<Country> countries = Arrays.asList(finland, austria);
        when(countryService.getCountries()).thenReturn(countries);

        CountryCovidData data = getCountryCovidData();
        countryDataList = Collections.singletonList(data);
        when(countryService.getCountryDataList(anyList(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(countryDataList);
    }
}
