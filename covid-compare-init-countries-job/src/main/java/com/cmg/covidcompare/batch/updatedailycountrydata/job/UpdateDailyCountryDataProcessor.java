package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import static java.lang.Float.parseFloat;

import com.cmg.covidcompare.client.WorldometerWebScraperFeignClient;
import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDate;
import com.cmg.covidcompare.domain.CountryDayData;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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
        if (countryDayData.getCases() == -1) {
            throw new CountryDayDataNotUpdatedYetException(country.getName());
        }
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
