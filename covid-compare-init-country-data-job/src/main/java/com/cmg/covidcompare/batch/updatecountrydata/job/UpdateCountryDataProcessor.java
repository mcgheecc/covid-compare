package com.cmg.covidcompare.batch.updatecountrydata.job;

import com.cmg.covidcompare.client.CovidApiClient;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDataDto;
import com.cmg.covidcompare.domain.CountryDate;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UpdateCountryDataProcessor implements ItemProcessor<CountryDate, CountryData> {

    private CovidApiClient covidApiClient;
    private CountryDataMapper mapper;

    @Autowired
    public UpdateCountryDataProcessor(CovidApiClient covidApiClient, CountryDataMapper mapper) {
        this.covidApiClient = covidApiClient;
        this.mapper = mapper;
    }

    public CountryData process(CountryDate countryDate) {

        log.info("Processing country {}", countryDate.getCountry().getName());

        Optional<CountryDataDto> optional
            = covidApiClient.getCountryStatus(countryDate.getCountry().getCountryCode(), countryDate.getDate());
        if (optional.isPresent()) {
            CountryDataDto countryDataDto = optional.get();
            return mapper.mapCountryData(countryDataDto, countryDate.getCountry());
        } else {
            log.info("No data returned for {}", countryDate.getCountry());
        }
        return new CountryData();
    }

}
