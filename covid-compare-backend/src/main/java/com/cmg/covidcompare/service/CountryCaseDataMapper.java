package com.cmg.covidcompare.service;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryCovidData;
import com.cmg.covidcompare.domain.NameValue;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CountryCaseDataMapper {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    CountryCovidData mapToCountryCaseData(String countryCode, Set<CountryData> countryStatusList) {
        CountryCovidData countryCovidData = new CountryCovidData();
        countryCovidData.setName(countryCode);
        NameValue[] nameValues = mapCasesToNameValues(countryStatusList);
        countryCovidData.setCases(nameValues);
        nameValues = mapCasesPerHundredThousandToNameValues(countryStatusList);
        countryCovidData.setCasesPerHundredThousand(nameValues);
        nameValues = mapDeathsToNameValues(countryStatusList);
        countryCovidData.setDeaths(nameValues);
        return countryCovidData;
    }

    private NameValue[] mapCasesToNameValues(Set<CountryData> countryStatusList) {
        List<NameValue> nameValues = new ArrayList<>();
        countryStatusList.forEach(status -> {
            int cases = Math.max(status.getCasesToday(), 0);
            NameValue nameValue = new NameValue(status.getDate().format(formatter), cases);
            nameValues.add(nameValue);
        });
        return nameValues.toArray(new NameValue[0]);
    }

    private NameValue[] mapCasesPerHundredThousandToNameValues(Set<CountryData> countryStatusList) {
        List<NameValue> nameValues = new ArrayList<>();
        countryStatusList.forEach(status -> {
            int cases = Math.max(status.getCasesPerHundredThousand(), 0);
            NameValue nameValue = new NameValue(status.getDate().format(formatter), cases);
            nameValues.add(nameValue);
        });
        return nameValues.toArray(new NameValue[0]);
    }

    private NameValue[] mapDeathsToNameValues(Set<CountryData> countryStatusList) {
        List<NameValue> nameValues = new ArrayList<>();
        countryStatusList.forEach(status -> {
            int deaths = Math.max(status.getDeathsToday(), 0);
            NameValue nameValue = new NameValue(status.getDate().format(formatter), deaths);
            nameValues.add(nameValue);
        });
        return nameValues.toArray(new NameValue[0]);
    }

}
