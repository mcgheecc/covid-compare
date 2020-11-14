package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    @JsonProperty("iso")
    private String countryCode;
    private String name;
    private LocalDate date;
}
