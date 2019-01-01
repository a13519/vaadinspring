package net.zousys.gba.batch.config;

import lombok.RequiredArgsConstructor;
import net.zousys.gba.batch.entity.JobDetails;
import net.zousys.gba.batch.entity.JobDetailsRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobDetailsRepository repository;

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new SampleTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job aJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("importStudents", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .build();

    }

    @Bean
    public org.springframework.batch.core.launch.JobLauncher myJobLauncher() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    /**
     *
     */
    public static class SampleTasklet implements Tasklet {
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            try {
                Thread.sleep(10000);
                System.out.println("done...");
            } catch (Exception e){
                e.printStackTrace();
            }
            return RepeatStatus.FINISHED;
        }
    }

    /**
     *
     */
    @Component
    public static class JobListener implements JobExecutionListener {
        @Autowired
        private JobDetailsRepository jobDetailsRepository;

        /**
         *
         * @param jobExecution the current {@link JobExecution}
         */
        public void beforeJob(JobExecution jobExecution) {
            JobDetails jobDetails = JobDetails.builder()
                    .id(jobExecution.getJobId())
                    .name(jobExecution.getJobInstance().getJobName())
                    .log("xxx")
                    .status(jobExecution.getStatus().toString())
                    .batchJobExeId(jobExecution.getJobId())
                    .build();
            jobDetailsRepository.save(jobDetails);
        }

        /**
         *
         * @param jobExecution the current {@link JobExecution}
         */
        public void afterJob(JobExecution jobExecution) {
            Optional<JobDetails> ojobDetails = jobDetailsRepository.findById(jobExecution.getId());
            JobDetails jobDetails = ojobDetails.get();
            if (jobDetails != null) {
                jobDetails.setStatus(jobExecution.getStatus().toString());
                jobDetailsRepository.save(jobDetails);
            }
        }
    }
}
