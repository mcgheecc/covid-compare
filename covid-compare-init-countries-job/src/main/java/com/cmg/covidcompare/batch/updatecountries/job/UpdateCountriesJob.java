package com.cmg.covidcompare.batch.updatecountries.job;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.domain.CountryDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "com.cmg.covidcompare.repository")
@EntityScan(basePackages = "com.cmg.covidcompare.domain")
@EnableFeignClients(basePackages = "com.cmg.covidcompare.client")
@ComponentScan(basePackages = {"com.cmg.covidcompare.client",
    "com.cmg.covidcompare.batch.updatecountries.job",
    "com.cmg.covidcompare.repository"})
public class UpdateCountriesJob {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private UpdateCountriesReader updateCountriesReader;

    private UpdateCountriesWriter updateCountriesWriter;

    private UpdateCountriesProcessor updateCountriesProcessor;

    @Autowired
    public UpdateCountriesJob(JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory,
        UpdateCountriesReader updateCountriesReader,
        UpdateCountriesWriter updateCountriesWriter,
        UpdateCountriesProcessor updateCountriesProcessor) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.updateCountriesReader = updateCountriesReader;
        this.updateCountriesWriter = updateCountriesWriter;
        this.updateCountriesProcessor = updateCountriesProcessor;
    }

    @Bean
    public Job updateCountries() {
        return jobBuilderFactory.get("updateCountries")
            .incrementer(new RunIdIncrementer())
            .flow(updateCountriesStep())
            .end().build();
    }

    @Bean
    public Step updateCountriesStep() {
        return stepBuilderFactory.get("updateCountriesStep")
            .<CountryDto, Country>chunk(50)
            .reader(updateCountriesReader)
            .processor(updateCountriesProcessor)
            .writer(updateCountriesWriter)
            .faultTolerant()
            .skipLimit(20)
            .skip(MissingPopulationException.class)
            .retry(feign.RetryableException.class)
            .retryLimit(20)
            .build();
    }

}
