package com.cmg.covidcompare.batch.updatecountrydata.job;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;

@Configuration
@EnableBatchProcessing
@IntegrationComponentScan
@EnableJpaRepositories("com.cmg.covidcompare.repository")
@EntityScan(basePackages = "com.cmg.covidcompare.domain")
@ComponentScan(basePackages = "com.cmg.covidcompare.client")
public class UpdateCountryDataJob {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private UpdateCountryDataReader updateCountryDataReader;

    private UpdateCountryDataWriter updateCountryDataWriter;

    private UpdateCountryDataProcessor updateCountryDataProcessor;

    public UpdateCountryDataJob(JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory, UpdateCountryDataReader updateCountryDataReader,
        UpdateCountryDataWriter updateCountryDataWriter,
        UpdateCountryDataProcessor updateCountryDataProcessor) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.updateCountryDataReader = updateCountryDataReader;
        this.updateCountryDataWriter = updateCountryDataWriter;
        this.updateCountryDataProcessor = updateCountryDataProcessor;
    }

    @Bean
    public TaskletStep updateCountryDataTaskletStep() {
        return stepBuilderFactory.get("updateCountryDataTaskletStep").
            <CountryDate, CountryData>chunk(10)
            .reader(updateCountryDataReader)
            .processor(updateCountryDataProcessor)
            .writer(updateCountryDataWriter)
            .build();
    }

    @Bean
    @Profile("master")
    public Job updateCountryData() {
        return jobBuilderFactory.get("updateCountryData")
            .incrementer(new RunIdIncrementer())
            .start(updateCountryDataTaskletStep())
            .build();
    }

}
