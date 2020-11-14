package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionData {

    @JsonProperty("data")
    private List<Country> countries = new ArrayList<>();
}
