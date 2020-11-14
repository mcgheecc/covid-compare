package com.cmg.covidcompare.service;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryCovidData;
import com.cmg.covidcompare.repository.CountryDataRepository;
import com.cmg.covidcompare.repository.CountryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CountryService {

    private final CountryDataRepository countryDataRepository;
    private final CountryRepository countryRepository;
    private final CountryCaseDataMapper mapper;

    public List<Country> getCountries() {
        return countryRepository.findAll(Sort.by("name").ascending());
    }

    public List<CountryCovidData> getCountryDataList(List<String> countryCodes, LocalDate startDate, LocalDate endDate) {
        List<CountryCovidData> countryCovidDataList = new ArrayList<>();

        countryCodes.forEach(countryCode -> {
            Set<CountryData> countryDataList = countryDataRepository.getForDatesAndCountry(countryCode, startDate, endDate);
            CountryCovidData countryCovidData = mapper.mapToCountryCaseData(countryCode, countryDataList);
            countryCovidDataList.add(countryCovidData);
        });

        int length = countryCovidDataList.get(0).getCases().length;

        log.info("Returning {} entries for {} between {} and {}", length, countryCodes, startDate, endDate);
        return countryCovidDataList;
    }

}
