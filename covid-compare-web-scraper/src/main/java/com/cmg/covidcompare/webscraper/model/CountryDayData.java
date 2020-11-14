package com.cmg.covidcompare.webscraper.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryDayData {

    private String name;
    private int cases;
    private float casesPerHundredThousand;
    private int deaths;
    private LocalDate date;
    private LocalDateTime lastUpdated;

}
