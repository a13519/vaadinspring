package net.zousys.gba.batch.service;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BatchService {
    @Autowired
    private JobExplorer jobExplorer;

    /**
     *
     * @param name
     * @return
     * @throws NoSuchJobException
     */
    public List<JobExecution> getExecutions(String name) throws NoSuchJobException {
        List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(name, 0, (int)jobExplorer.getJobInstanceCount(name));
        Collections.sort(jobInstances, new Comparator<JobInstance>() {
            @Override
            public int compare(JobInstance jobInstance, JobInstance t1) {
                return jobInstance.getVersion().compareTo(t1.getVersion());
            }
        });
        return jobExplorer.getJobExecutions(jobInstances.get(0));
    }
}
