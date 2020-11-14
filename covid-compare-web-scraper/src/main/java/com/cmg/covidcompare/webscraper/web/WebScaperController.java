package com.cmg.covidcompare.webscraper.web;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import com.cmg.covidcompare.webscraper.service.WorldometerScraper;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scraper")
@RequiredArgsConstructor
public class WebScaperController {

    private final WorldometerScraper worldometerScraper;

    @GetMapping("/worldometer/{countryName}/{date}")
    public CountryDayData getWorldometerCountryDayData( @PathVariable String countryName,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Optional<CountryDayData> countryDayData = worldometerScraper.getStats(countryName, date);
        return countryDayData.orElseGet(() -> new CountryDayData(countryName, -1, 0,0, date, null));
    }

    @GetMapping("/worldometer/population/{countryName}")
    public Integer getWorldometerCountryPopulation(@PathVariable String countryName) {
        Optional<Integer> countryPopulation = worldometerScraper.getPopulation(countryName);
        return countryPopulation.orElse(0);
    }
}
