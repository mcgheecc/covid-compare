package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDataDto {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private int confirmed;
    private int deaths;
    private int recovered;
    @JsonProperty("confirmed_diff")
    private int casesToday;
    @JsonProperty("deaths_diff")
    private int deathsToday;
    @JsonProperty("recovered_diff")
    private int recoveredToday;
    @JsonProperty("last_update")
    private String lastUpdated;
    private int active;
    @JsonProperty("active_diff")
    private int activeDiff;
    @JsonProperty("fatality_rate")
    private double fatalityRate;
    private RegionDto region;

}
