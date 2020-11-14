package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryDate;
import com.cmg.covidcompare.repository.CountryRepository;
import com.cmg.covidcompare.util.LocalDateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@JobScope
public class UpdateDailyCountryDataReader implements ItemReader<CountryDate> {

    private List<CountryDate> countryDateList;
    private CountryRepository countryRepository;
    private int nextCountryIndex;
    private Date jobParameterDate;

    @Autowired
    public UpdateDailyCountryDataReader(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryDate read() {
        if (isNotInitialized()) {
            countryDateList = new ArrayList<>();
            List<Country> initialCountryList = countryRepository.findAll(Sort.by("name").ascending());

            if (jobParameterDate == null) {
                LocalDate tomorrow = LocalDate.now().plusDays(1);
                initialCountryList.forEach(country -> {
                    LocalDate latestUpdate = LocalDateUtil.toLocalDate(country.getLatestUpdate());
                    latestUpdate.datesUntil(tomorrow).forEach(localDate -> {
                        CountryDate countryDate = new CountryDate(country, localDate);
                        countryDateList.add(countryDate);
                    });
                });
            } else {
                initialCountryList.forEach(country -> {
                    LocalDate localDate = LocalDateUtil.toLocalDate(jobParameterDate);
                    CountryDate countryDate = new CountryDate(country, localDate);
                    countryDateList.add(countryDate);
                });
            }
            log.info("Initialized {} country/date records", countryDateList.size());
        }

        CountryDate nextCountry = null;

        if (nextCountryIndex < countryDateList.size()) {
            nextCountry = countryDateList.get(nextCountryIndex);
            nextCountryIndex++;
        } else {
            nextCountryIndex = 0;
        }

        log.info("Returning next country {}", nextCountry);

        return nextCountry;
    }

    private boolean isNotInitialized() {
        return this.countryDateList == null;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
        parameters.getDate("data");
        //use your parameters
    }
}
