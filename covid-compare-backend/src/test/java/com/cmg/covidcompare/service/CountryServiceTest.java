package com.cmg.covidcompare.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryCovidData;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.NameValue;
import com.cmg.covidcompare.repository.CountryDataRepository;
import com.cmg.covidcompare.repository.CountryRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryDataRepository countryDataRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CountryCaseDataMapper mapper;

    @InjectMocks
    private CountryService countryService;

    private Country finland = new Country("FIN", "Finland", new Date(), 5543667);
    private Country austria = new Country("AUT", "Austria", new Date(), 9023909);
    private List<Country> countries = Arrays.asList(finland, austria);
    private List<String> countryCodes = Arrays.asList("FIN", "AUT");

    private NameValue[] CASES_NAME_VALUES = {new NameValue("2020-10-13", 2930)};
    private NameValue[] CASES_PHT_NAME_VALUES = {new NameValue("2020-10-13", 1)};
    private NameValue[] DEATHS_NAME_VALUES = {new NameValue("2020-10-13", 10)};

    @Test
    void getCountries() {
        when(countryRepository.findAll(any(Sort.class))).thenReturn(countries);
        List<Country> actual = countryService.getCountries();

        assertThat(actual).contains(finland, austria);
        assertThat(actual.get(0)).isEqualTo(finland);
        assertThat(actual.get(1)).isEqualTo(austria);
    }

    @Test
    void getCountryDataList() {

        CountryData countryData = new CountryData();
        countryData.setCasesPerHundredThousand(10);
        countryData.setCasesToday(100);
        countryData.setDeaths(3);
        Set<CountryData> countryDataList = new HashSet<>(Collections.singletonList(countryData));
        CountryCovidData finlandData = getCountryCovidData(finland.getName());
        CountryCovidData austriaData = getCountryCovidData(austria.getName());

        when(mapper.mapToCountryCaseData(any(String.class), anySet()))
            .thenReturn(finlandData).thenReturn(austriaData);
        when(countryDataRepository
            .getForDatesAndCountry(any(String.class), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(countryDataList);

        List<CountryCovidData> actual = countryService
            .getCountryDataList(countryCodes, LocalDate.now(), LocalDate.now().plusDays(1));

        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(2);
    }

    private CountryCovidData getCountryCovidData(String countryName) {
        CountryCovidData data = new CountryCovidData();
        data.setName(countryName);
        data.setCases(CASES_NAME_VALUES);
        data.setCasesPerHundredThousand(CASES_PHT_NAME_VALUES);
        data.setDeaths(DEATHS_NAME_VALUES);
        return data;
    }

}
