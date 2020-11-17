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
        int casesToday = 0;
        int deathsToday = 0;

        if (containsCases(element)) {
            casesToday = Integer.parseInt(list.get(0));
            deathsToday = list.size() > 1 ? Integer.parseInt(list.get(1)) : 0;
        } else if (containsDeaths(element)) {
            deathsToday = Integer.parseInt(list.get(0));
        }
        return new CountryDayData(countryName, casesToday, 0, deathsToday, date, LocalDateTime.now());
    }

    private List<String> extractStrings(DomElement element) {
        return Arrays.stream(element.asText().replaceAll("[,\\n]", "")
            .split("\\D")).filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

    private boolean containsCases(DomElement element) {
        return element.asText().contains("new cases");
    }

    private boolean containsDeaths(DomElement element) {
        return element.asText().contains("new deaths");
    }


}
