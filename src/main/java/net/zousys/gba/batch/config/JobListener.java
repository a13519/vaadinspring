package net.zousys.gba.batch.config;

import net.zousys.gba.batch.entity.JobDetails;
import net.zousys.gba.batch.repository.JobDetailsRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JobListener implements JobExecutionListener {
    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    /**
     *
     * @param jobExecution the current {@link JobExecution}
     */
    public void beforeJob(JobExecution jobExecution) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication==null?"":authentication.getName()+""; // Get the username of the authenticated user
        String authorities = authentication==null?"":authentication.getAuthorities().toString()+"";

        JobDetails jobDetails = JobDetails.builder()
                .id(jobExecution.getJobId())
                .name(jobExecution.getJobInstance().getJobName())
//                .log("xxx")
                .parameters(jobExecution.getJobParameters().toString())
                .status(jobExecution.getStatus().toString())
                .batchJobExeId(jobExecution.getJobId())
                .build();
        jobDetails.setUsername(username);
        jobDetails.setAuthority(authorities);
        jobDetailsRepository.save(jobDetails);
    }

    /**
     *
     * @param jobExecution the current {@link JobExecution}
     */
    public void afterJob(JobExecution jobExecution) {
        Optional<JobDetails> ojobDetails = jobDetailsRepository.findById(jobExecution.getJobId());
        JobDetails jobDetails = ojobDetails.get();
        if (jobDetails != null) {
            jobDetails.setStatus(jobExecution.getStatus().toString());
            jobDetailsRepository.save(jobDetails);
        }
    }
}
