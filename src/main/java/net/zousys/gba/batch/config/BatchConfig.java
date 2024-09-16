package net.zousys.gba.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    @Bean
    public org.springframework.batch.core.launch.JobLauncher myJobLauncher() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }

}
