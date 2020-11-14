package com.cmg.covidcompare.client;

import com.cmg.covidcompare.domain.CountryDayData;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "worldometerWebScraperFeignClient", url = "${feign.url}")
public interface WorldometerWebScraperFeignClient {

    @GetMapping("/api/v1/scraper/worldometer/{countryName}/{date}")
    CountryDayData getWorldometerCountryDayData(@PathVariable("countryName") String countryName,
        @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/api/v1/scraper/worldometer/population/{countryName}")
    Integer getWorldometerCountryPopulation(@PathVariable("countryName") String countryName) ;
}

