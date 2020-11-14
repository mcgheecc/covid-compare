package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CountryStatus {

    private String country;
    private String date;
    private int cases;
    private int deaths;
    private int recovered;
    @JsonProperty("last_update")
    private String lastUpdate;
    @JsonProperty("new_cases")
    private int newCases;
    @JsonProperty("new_deaths")
    private int newDeaths;
}
