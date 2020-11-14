package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDate;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
@EnableScheduling
@EnableJpaRepositories("com.cmg.covidcompare.repository")
@EntityScan(basePackages = "com.cmg.covidcompare.domain")
@EnableFeignClients(basePackages = "com.cmg.covidcompare.client")
public class UpdateDailyCountryDataJob {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private UpdateDailyCountryDataReader reader;

    private UpdateDailyCountryDataWriter writer;

    private UpdateDailyCountryDataProcessor processor;

    @Bean
    public Job updateDailyCountryData() {
        return jobBuilderFactory.get("updateDailyCountryData")
            .incrementer(new RunIdIncrementer())
            .flow(updateDailyCountryDataStep())
            .end().build();
    }

    @Bean
    public Step updateDailyCountryDataStep() {
        return stepBuilderFactory.get("updateDailyCountryDataStep")
            .<CountryDate, CountryData>chunk(1)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .faultTolerant()
            .skipLimit(300)
            .skip(CountryDayDataNotUpdatedYetException.class)
            .build();
    }
}
