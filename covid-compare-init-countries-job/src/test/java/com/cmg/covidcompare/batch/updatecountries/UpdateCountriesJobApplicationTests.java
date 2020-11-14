package com.cmg.covidcompare.batch.updatecountries;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan(basePackages = "com.cmg.covidcompare")
class UpdateCountriesJobApplicationTests {

    //@Test
    void contextLoads() {
    }

}
