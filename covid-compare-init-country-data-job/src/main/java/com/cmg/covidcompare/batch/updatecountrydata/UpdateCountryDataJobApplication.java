package com.cmg.covidcompare.batch.updatecountrydata;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
public class UpdateCountryDataJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdateCountryDataJobApplication.class, args);
    }

}
