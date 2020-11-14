package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryResponse {

    @JsonProperty("data")
    private List<CountryDto> countries = new ArrayList<>();
}
