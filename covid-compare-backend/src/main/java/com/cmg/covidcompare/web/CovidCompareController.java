package com.cmg.covidcompare.web;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryCovidData;
import com.cmg.covidcompare.service.CountryService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/covid")
public class CovidCompareController {

    private final CountryService countryService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public CovidCompareController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public List<Country> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping("/data/{countryCodes}/{startDate}/{endDate}")
    public List<CountryCovidData> getCountyData(@PathVariable List<String> countryCodes, @PathVariable String startDate,
        @PathVariable String endDate) {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return countryService.getCountryDataList(countryCodes, start, end);
    }
}
