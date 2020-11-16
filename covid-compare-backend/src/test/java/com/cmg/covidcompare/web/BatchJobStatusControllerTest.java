package com.cmg.covidcompare.web;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.cmg.covidcompare.domain.BatchJobExecution;
import com.cmg.covidcompare.repository.BatchJobExecutionRepository;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BatchJobStatusControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String JOB_NAME = "testJob";

    @MockBean
    private BatchJobExecutionRepository batchJobExecutionRepository;

    @Test
    void getUpdateCountryJobStatus_givenEmptyList_expectUnknownStatus() {
        when(batchJobExecutionRepository.findByLatestByJobName(any(String.class))).thenReturn(Collections.emptyList());

        String url = String.format("http://localhost:%s/batch-jobs/status/%s", port, JOB_NAME);
        var map = restTemplate.getForObject(url, Map.class);

        assertThat(map).containsEntry("status", "UNKNOWN");
    }

    @Test
    void getUpdateCountryJobStatus_givenList_expectStatusReturned() {
        String expectedStatus = "RUNNING";
        BatchJobExecution execution = new BatchJobExecution();
        execution.setStatus(expectedStatus);
        when(batchJobExecutionRepository.findByLatestByJobName(any(String.class)))
            .thenReturn(Collections.singletonList(execution));

        String url = String.format("http://localhost:%s/batch-jobs/status/%s", port, JOB_NAME);
        var map = restTemplate.getForObject(url, Map.class);

        assertThat(map).containsEntry("status", expectedStatus);
    }

}
