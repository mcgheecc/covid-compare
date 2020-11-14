package com.cmg.covidcompare.batch.updatecountrydata.job;

import static java.lang.Float.*;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDataDto;
import org.springframework.stereotype.Component;

@Component
public class CountryDataMapper {

    CountryData mapCountryData(CountryDataDto countryDataDto, Country country) {
        CountryData countryData = new CountryData();
        countryData.setCountry(country);
        countryData.setActive(countryDataDto.getActive());
        countryData.setActiveDiff(countryDataDto.getActiveDiff());
        countryData.setCasesToday(countryDataDto.getCasesToday());
        countryData.setConfirmed(countryDataDto.getConfirmed());
        countryData.setDate(countryDataDto.getDate());
        countryData.setDeaths(countryDataDto.getDeaths());
        countryData.setDeathsToday(countryDataDto.getDeathsToday());
        countryData.setFatalityRate(countryDataDto.getFatalityRate());
        countryData.setLastUpdated(countryDataDto.getLastUpdated());
        countryData.setRecovered(countryDataDto.getRecovered());
        countryData.setRecoveredToday(countryDataDto.getRecoveredToday());
        int casesPerHundredThousand = getCasesPerHundredThousand(country, countryDataDto.getCasesToday());
        countryData.setCasesPerHundredThousand(casesPerHundredThousand);
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
