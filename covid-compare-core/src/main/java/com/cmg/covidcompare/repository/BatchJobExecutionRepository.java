package com.cmg.covidcompare.repository;

import com.cmg.covidcompare.domain.BatchJobExecution;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, String> {

    @Query("from BatchJobExecution e inner join e.batchJobInstance i where i.jobName = :jobName order by e.createTime desc ")
    List<BatchJobExecution> findByLatestByJobName(@Param("jobName") String jobName);
}
