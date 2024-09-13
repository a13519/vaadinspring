package net.zousys.gba.batch.config;

import net.zousys.gba.batch.entity.JobDetails;
import org.springframework.batch.item.ItemProcessor;

public class JobDetailsProcessor implements ItemProcessor<JobDetails,JobDetails> {

    @Override
    public JobDetails process(JobDetails student) {
        return student;
    }
}
