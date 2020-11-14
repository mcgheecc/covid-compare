package com.cmg.covidcompare.batch.updatecountrydata.job;

import com.cmg.covidcompare.repository.CountryDataRepository;
import com.cmg.covidcompare.domain.CountryData;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UpdateCountryDataWriter implements ItemWriter<CountryData> {

    private CountryDataRepository countryDataRepository;

    @Autowired
    public UpdateCountryDataWriter(CountryDataRepository countryDataRepository) {
        this.countryDataRepository = countryDataRepository;
    }

    @Override
    public void write(List<? extends CountryData> items) {
        countryDataRepository.saveAll(items);
    }
}
