package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.repository.CountryDataRepository;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateDailyCountryDataWriter implements ItemWriter<CountryData> {

    private CountryDataRepository countryDataRepository;

    @Autowired
    public UpdateDailyCountryDataWriter(CountryDataRepository countryDataRepository) {
        this.countryDataRepository = countryDataRepository;
    }

    @Override
    public void write(List<? extends CountryData> items) {
        countryDataRepository.saveAll(items);
    }
}
