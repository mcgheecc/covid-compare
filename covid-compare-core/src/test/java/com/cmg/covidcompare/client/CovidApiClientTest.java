package com.cmg.covidcompare.client;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.domain.CountryDataDto;
import com.cmg.covidcompare.domain.CountryDataResponse;
import com.cmg.covidcompare.domain.CountryDto;
import com.cmg.covidcompare.domain.CountryResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
class CovidApiClientTest {

    @MockBean
    private RestTemplate restTemplate;

    private CovidApiClient client = new CovidApiClient();

    private final static String URL = "http://localhost:8080";

    @Test
    void getCountries() {
        client.setRestTemplate(restTemplate);
        client.setCountryUrl(URL);
        CountryResponse expected = getCountryResponse();

        when(restTemplate.getForObject(URL,  CountryResponse.class)).thenReturn(expected);

        CountryResponse actual = client.getCountries();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCountryStatus() {
        client.setRestTemplate(restTemplate);
        client.setReportUrl(URL);
        CountryDataResponse response = getCountryDataResponse();
        CountryDataDto expected = response.getData().get(0);
        when(restTemplate.getForObject(URL,  CountryDataResponse.class)).thenReturn(response);

        Optional<CountryDataDto> optional = client.getCountryStatus("AUT", LocalDate.now());

        assertThat(optional).isPresent();
        CountryDataDto actual = optional.get();
        assertThat(actual.getCasesToday()).isEqualTo(expected.getCasesToday());
        assertThat(actual.getDeathsToday()).isEqualTo(expected.getDeathsToday());
    }

    private CountryResponse getCountryResponse() {
        CountryResponse response = new CountryResponse();
        CountryDto country = new CountryDto();
        country.setCountryCode("AUT");
        country.setName("Austria");
        country.setDate(LocalDate.now());
        List<CountryDto> countries = Collections.singletonList(country);
        response.setCountries(countries);
        return response;
    }

    private CountryDataResponse getCountryDataResponse() {
        CountryDataResponse response = new CountryDataResponse();
        CountryDataDto countryData = new CountryDataDto();
        countryData.setCasesToday(50);
        countryData.setDeathsToday(1);
        List<CountryDataDto> data = Collections.singletonList(countryData);
        response.setData(data);
        return response;
    }
}
