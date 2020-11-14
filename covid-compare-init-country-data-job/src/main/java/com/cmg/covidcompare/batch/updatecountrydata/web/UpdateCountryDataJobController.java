package com.cmg.covidcompare.batch.updatecountrydata.web;

import java.util.Collections;
import java.util.Date;
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
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("master")
@RestController
@RequestMapping("/job/start")
@Log4j2
public class UpdateCountryDataJobController {

    private JobLauncher jobLauncher;
    @Autowired(required = false)
    private Job updateCountryDataJob;

    @Autowired
    public UpdateCountryDataJobController(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/updateCountryDataJob/{startDate}/{endDate}")
    public Map<String, String> startUpdateCountryDataJob(
        @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        JobParameters jobParameters
            = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addDate("startDate", startDate)
            .addDate("endDate", endDate)
            .toJobParameters();
        log.info("Calling start job: updateCountryDataJob with params {}", jobParameters);
        return startJob(updateCountryDataJob, jobParameters);
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
