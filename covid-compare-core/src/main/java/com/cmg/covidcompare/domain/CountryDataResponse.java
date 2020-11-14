package com.cmg.covidcompare.domain;

import java.util.List;
import lombok.Data;

@Data
public class CountryDataResponse {

    private List<CountryDataDto> data;
}
