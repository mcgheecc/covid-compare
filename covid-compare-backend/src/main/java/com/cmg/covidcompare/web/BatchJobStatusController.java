package com.cmg.covidcompare.web;

import com.cmg.covidcompare.domain.BatchJobExecution;
import com.cmg.covidcompare.repository.BatchJobExecutionRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch-jobs/status/")
class BatchJobStatusController {

    private BatchJobExecutionRepository batchJobExecutionRepository;

    @Autowired
    public BatchJobStatusController(BatchJobExecutionRepository batchJobExecutionRepository) {
        this.batchJobExecutionRepository = batchJobExecutionRepository;
    }

    @GetMapping("/{jobName}")
    public Map<String, String> getUpdateCountryJobStatus(@PathVariable String jobName) {
        return getStatus(jobName);
    }

    private Map<String, String> getStatus(String jobName) {
        List<BatchJobExecution> executions = batchJobExecutionRepository.findByLatestByJobName(jobName);
        if (! executions.isEmpty()) {
            return  Collections.singletonMap("status", executions.get(0).getStatus());
        }
        return Collections.singletonMap("status", "UNKNOWN");
    }
}
