package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import static java.lang.Float.parseFloat;

import com.cmg.covidcompare.client.WorldometerWebScraperFeignClient;
import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDate;
import com.cmg.covidcompare.domain.CountryDayData;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UpdateDailyCountryDataProcessor implements ItemProcessor<CountryDate, CountryData> {

    private WorldometerWebScraperFeignClient feignClient;

    @Autowired
    public UpdateDailyCountryDataProcessor(WorldometerWebScraperFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public CountryData process(CountryDate countryDate) {
        Country country = countryDate.getCountry();
        CountryDayData countryDayData = feignClient.getWorldometerCountryDayData(country.getName(), countryDate.getDate());
        if (countryDayData.getCases() == -1 || countryDayData.getDate() == null) {
            throw new CountryDayDataNotUpdatedYetException(country.getName());
        }
        log.info("Received {} from API:", countryDayData.toString());
        CountryData countryData = new CountryData();
        countryData.setDeathsToday(countryDayData.getDeaths());
        countryData.setCasesToday(countryDayData.getCases());
        countryData.setCasesPerHundredThousand(getCasesPerHundredThousand(country, countryDayData.getCases()));
        countryData.setDate(countryDayData.getDate());
        countryData.setCountry(country);
        return countryData;
    }

    private int getCasesPerHundredThousand(Country country, int cases) {
        if (cases <= 0) {
            return 0;
        }
        float fCases = parseFloat("" + cases);
        float fPopulation = parseFloat("" + country.getPopulation());
        float casesPerHundredThousand = (fCases / fPopulation)  * 100000;
        return (int) casesPerHundredThousand;
    }
}
