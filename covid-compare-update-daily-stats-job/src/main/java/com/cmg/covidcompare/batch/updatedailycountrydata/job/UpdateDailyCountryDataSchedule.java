package com.cmg.covidcompare.batch.updatedailycountrydata.job;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateDailyCountryDataSchedule {

    private JobLauncher jobLauncher;
    private Job updateDailyCountryDataJob;

    @Scheduled(cron = "${batch.updateDailyCountryData.cron}")
    public void schedule() throws Exception {
        JobParameters jobParameters
            = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(updateDailyCountryDataJob, jobParameters);
    }
}
