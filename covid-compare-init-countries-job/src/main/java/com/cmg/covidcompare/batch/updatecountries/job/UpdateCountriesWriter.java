package com.cmg.covidcompare.batch.updatecountries.job;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.repository.CountryRepository;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCountriesWriter implements ItemWriter<Country> {

    private CountryRepository countryRepository;

    @Autowired
    public UpdateCountriesWriter(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void write(List<? extends Country> countries) {
        countryRepository.saveAll(countries);
    }
}
