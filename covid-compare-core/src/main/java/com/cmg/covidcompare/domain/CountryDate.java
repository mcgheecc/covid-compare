package com.cmg.covidcompare.domain;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CountryDate implements Serializable {

    private Country country;
    private LocalDate date;
}
