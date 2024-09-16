package net.zousys.gba.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchJobA {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobListener jobListener;
    private final StepListener stepListener;

    @Bean
    public Step stepa1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("batchA-Step1", jobRepository)
                .tasklet(new SampleTaskletA(), transactionManager)
                .listener(stepListener)
                .build();
    }

    @Bean
    public Step stepa2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("batchA-Step2", jobRepository)
                .tasklet(new SampleTaskletA(), transactionManager)
                .listener(stepListener)
                .build();
    }

    @Bean
    public Job aJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("BatchJob_A", jobRepository)
                .start(stepa1(jobRepository, transactionManager))
                .next(stepa2(jobRepository, transactionManager))
                .listener(jobListener)
                .build();

    }

    /**
     *
     */
    public static class SampleTaskletA implements Tasklet {
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


}
