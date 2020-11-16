package com.cmg.covidcompare.batch.updatecountries.job;

import com.cmg.covidcompare.client.CovidApiClient;
import com.cmg.covidcompare.domain.CountryDto;
import java.util.List;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class UpdateCountriesReader implements ItemReader<CountryDto> {

    private CovidApiClient covidApiClient;
    private List<CountryDto> countryList;
    private int nextCountryIndex;

    @Autowired
    public UpdateCountriesReader(CovidApiClient covidApiClient) {
        this.covidApiClient = covidApiClient;
    }

    @Override
    public CountryDto read() {
        if (isNotInitialized()) {
            countryList = covidApiClient.getCountries().getCountries();
        }

        CountryDto nextCountry = null;

        if (nextCountryIndex < countryList.size()) {
            nextCountry = countryList.get(nextCountryIndex);
            nextCountryIndex++;
        }
        return nextCountry;
    }

    private boolean isNotInitialized() {
        return this.countryList == null;
    }

}
