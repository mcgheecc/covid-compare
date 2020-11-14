package com.cmg.covidcompare.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BatchJobExecution {
    @Id
    @Column(name = "job_execution_id")
    private Long jobExecutionId;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "exit_code")
    private String exitCode;

    @Column(name = "exit_message")
    private String exitMessage;

    @Column(name = "job_configuration_location")
    private String jobConfigurationLocation;

    @Column(name = "last_updated")
    private Timestamp lastUpdated;

    @Column(name = "start_time")
    private Timestamp startTime;

    private String status;

    private Long version;

    @ManyToOne
    @JoinColumn(name = "job_instance_id")
    private BatchJobInstance batchJobInstance;
}
