package com.cmg.covidcompare.batch.updatecountrydata.job;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.cmg.covidcompare.batch.updatecountrydata.job.CountryDataMapper;
import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDataDto;
import org.junit.jupiter.api.Test;

class CountryDataMapperTest {

    private CountryDataMapper mapper = new CountryDataMapper();

    @Test
    void mapCountryData() {
        CountryDataDto countryDataDto = new CountryDataDto();
        countryDataDto.setCasesToday(5349);
        Country country = new Country();
        country.setCountryCode("AUT");
        country.setName("Austria");
        country.setPopulation(9006398);
        CountryData countryData = mapper.mapCountryData(countryDataDto, country);

        assertThat(countryData.getCasesPerHundredThousand()).isEqualTo(59);
    }
}
