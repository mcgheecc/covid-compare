package com.cmg.covidcompare.batch.updatecountries.job;

class MissingPopulationException extends RuntimeException {

    MissingPopulationException(String countryName) {
        super(String.format("No population data available for %s", countryName));
    }
}
