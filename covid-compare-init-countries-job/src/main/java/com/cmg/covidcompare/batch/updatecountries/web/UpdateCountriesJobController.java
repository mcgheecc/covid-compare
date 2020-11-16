package com.cmg.covidcompare.batch.updatecountries.web;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/job/start")
public class UpdateCountriesJobController {

    private JobLauncher jobLauncher;

    private Job updateCountriesJob;

    @Autowired
    public UpdateCountriesJobController(JobLauncher jobLauncher, Job updateCountriesJob) {
        this.jobLauncher = jobLauncher;
        this.updateCountriesJob = updateCountriesJob;
    }

    @GetMapping("/updateCountriesJob")
    public Map<String, String> startUpdateCountriesJob() {
        JobParameters jobParameters
            = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
         return startJob(updateCountriesJob, jobParameters);
    }

    private Map<String, String> startJob(Job job, JobParameters jobParameters) {
        CompletableFuture.supplyAsync(() ->
        {
            try {
                return jobLauncher.run(job, jobParameters);
            } catch (JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                log.error("Error starting updateCountryDataJob", e);
            }
            return null;
        });
        return Collections.singletonMap("message", String.format("Batch job %s has been requested to start", job.getName()));
    }

}
