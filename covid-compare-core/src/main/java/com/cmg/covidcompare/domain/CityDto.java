package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
class CityDto {

    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private int fips;
    @JsonProperty("lat")
    private float latitude;
    @JsonProperty("long")
    private float longitude;
    private int confirmed;
    private int deaths;
    @JsonProperty("confirmed_diff")
    private int casesToday;
    @JsonProperty("deaths_diff")
    private int deathsToday;
    @JsonProperty("last_update")
    private String lastUpdated;

}
