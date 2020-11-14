package com.cmg.covidcompare.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDayData {

    private String name;
    private int cases;
    private int casesPerHundredThousand;
    private int deaths;
    private LocalDate date;
    private LocalDateTime lastUpdated;

}
