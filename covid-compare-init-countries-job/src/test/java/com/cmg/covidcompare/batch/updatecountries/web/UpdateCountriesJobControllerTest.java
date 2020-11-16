package com.cmg.covidcompare.batch.updatecountries.web;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.cmg.covidcompare.domain.Country;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UpdateCountriesJobControllerTest {
    @LocalServerPort
    private int port;

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private Job updateCountriesJob;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void startUpdateCountriesJob() {

        String url = String.format("http://localhost:%s/job/start/updateCountriesJob", port);
        var map = restTemplate.getForObject(url, Map.class);

        assertThat(map).containsKeys("message");
    }
}
