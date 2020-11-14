package com.cmg.covidcompare.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class BatchJobInstance {

    @Id
    @Column(name = "job_instance_id")
    private Long jobInstanceId;

    @Column(name = "job_key")
    private String jobKey;

    @Column(name = "job_name")
    private String jobName;

    private Long version;

    @OneToMany(mappedBy = "batchJobInstance")
    private List<BatchJobExecution> batchJobExecutions;
}
