package com.cmg.covidcompare.domain;

import lombok.Data;

@Data
public class CountryCovidData {

    private String name;
    private NameValue[] cases;
    private NameValue[] casesPerHundredThousand;
    private NameValue[] deaths;

}
