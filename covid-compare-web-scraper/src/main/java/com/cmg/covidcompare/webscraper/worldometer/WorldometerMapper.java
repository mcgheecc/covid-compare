package com.cmg.covidcompare.webscraper.worldometer;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import com.gargoylesoftware.htmlunit.html.DomElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorldometerMapper {

    public CountryDayData mapCountryDayData(DomElement element, String countryName, LocalDate date) {
        List<String> list = extractStrings(element);
        int casesToday = Integer.parseInt(list.get(0));
        int deathsToday = list.size() > 1 ? Integer.parseInt(list.get(1)) : 0;
        return new CountryDayData(countryName, casesToday, 0, deathsToday, date, LocalDateTime.now());
    }

    private List<String> extractStrings(DomElement element) {
        return Arrays.stream(element.asText().replaceAll("[,\\n]", "")
            .split("\\D")).filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

}
