package net.zousys.gba.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchB {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobListener jobListener;
    @Bean
    public Step stepb1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("batchB-Step1", jobRepository)
                .tasklet(new SampleTaskletB(), transactionManager)
                .build();
    }

    @Bean
    public Job bJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("BatchJob_B", jobRepository)
                .start(stepb1(jobRepository, transactionManager))
                .listener(jobListener)
                .build();

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
    public static class SampleTaskletB implements Tasklet {
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
