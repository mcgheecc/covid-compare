package com.cmg.covidcompare.batch.updatecountries.job;

import com.cmg.covidcompare.client.WorldometerWebScraperFeignClient;
import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryDto;
import java.util.Date;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class UpdateCountriesProcessor implements ItemProcessor<CountryDto, Country> {

    @Autowired
    private WorldometerWebScraperFeignClient client;

    @Override
    public Country process(CountryDto countryDto) {
        Integer population = client.getWorldometerCountryPopulation(countryDto.getName());
        if (population == 0) {
            throw new MissingPopulationException(countryDto.getName());
        }
        return new Country(countryDto.getCountryCode(), countryDto.getName(), new Date(), population);
    }
}
