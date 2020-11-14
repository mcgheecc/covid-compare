package com.cmg.covidcompare.batch.updatedailycountrydata.job;

public class CountryDayDataNotUpdatedYetException extends RuntimeException {

    public CountryDayDataNotUpdatedYetException(String countryName) {
        super(String.format("No daily covid data available yet for %s", countryName));
    }
}
