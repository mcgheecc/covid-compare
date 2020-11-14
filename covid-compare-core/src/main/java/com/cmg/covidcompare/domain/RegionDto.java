package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;


@Data
public class RegionDto {

    @JsonProperty("iso")
    private String countryCode;
    private String name;
    private String province;
    @JsonProperty("lat")
    private float latitude;
    @JsonProperty("long")
    private float longitude;
    private List<CityDto> cities;
}
