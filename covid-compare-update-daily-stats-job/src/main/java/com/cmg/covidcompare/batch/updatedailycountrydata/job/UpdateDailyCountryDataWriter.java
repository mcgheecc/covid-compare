package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.repository.CountryDataRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UpdateDailyCountryDataWriter implements ItemWriter<CountryData> {

    private CountryDataRepository countryDataRepository;

    @Autowired
    public UpdateDailyCountryDataWriter(CountryDataRepository countryDataRepository) {
        this.countryDataRepository = countryDataRepository;
    }

    @Override
    public void write(List<? extends CountryData> items) {
        items.forEach((c) ->log.info("Attempting to write items: {}", c.toString()));
        countryDataRepository.saveAll(items);
    }
}
