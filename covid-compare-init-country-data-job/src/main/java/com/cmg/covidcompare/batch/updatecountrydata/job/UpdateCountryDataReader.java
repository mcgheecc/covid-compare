package com.cmg.covidcompare.batch.updatecountrydata.job;

import com.cmg.covidcompare.domain.CountryDate;
import com.cmg.covidcompare.repository.CountryRepository;
import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.util.LocalDateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@JobScope
public class UpdateCountryDataReader implements ItemReader<CountryDate> {

    private List<CountryDate> countryDateList;

    private int nextCountryIndex;

    private CountryRepository countryRepository;

    @Value("#{jobParameters['startDate']}")
    private Date startDate;

    @Value("#{jobParameters['endDate']}")
    private Date endDate;

    public UpdateCountryDataReader(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryDate read() {
        if (isNotInitialized()) {
            log.info("Initializing country list.");
            List<Country> initialCountryList = countryRepository.findAll(Sort.by("name").ascending());

            countryDateList = new ArrayList<>();
            for (Country country : initialCountryList) {
                Stream<LocalDate> dateStream = LocalDateUtil.toLocalDate(startDate).datesUntil(LocalDateUtil.toLocalDate(endDate));

                dateStream.forEach(date -> {
                    CountryDate countryDate = new CountryDate(country, date);
                    countryDateList.add(countryDate);
                });
            }
            log.info("Initialized {} country/date records", countryDateList.size());
        }

        CountryDate nextCountryDate = null;

        if (nextCountryIndex < countryDateList.size()) {
            nextCountryDate = countryDateList.get(nextCountryIndex);
            nextCountryIndex++;
        } else {
            nextCountryIndex = 0;
        }

        log.info("Returning next country {}", nextCountryDate);

        return nextCountryDate;
    }

    private boolean isNotInitialized() {
        return this.countryDateList == null;
    }

}
