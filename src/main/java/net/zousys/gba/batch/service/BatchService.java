package net.zousys.gba.batch.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BatchService {
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobLauncher myJobLauncher;
    @Autowired
    private Job aJob;

    /**
     *
     * @param para
     */
    public void runJob(String para) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("ENV", para).toJobParameters();
            JobInstance jobInstance = jobExplorer.getJobInstance(aJob.getName(), jobParameters);

            JobExecution jobExecution = myJobLauncher.run(aJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param name
     * @return
     * @throws NoSuchJobException
     */
    public List<JobExecution> getExecutions(String name) throws NoSuchJobException {
        List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(name, 0, (int)jobExplorer.getJobInstanceCount(name));
        if (jobInstances==null || jobInstances.size()==0) {
            return null;
        }
        Collections.sort(jobInstances, new Comparator<JobInstance>() {
            @Override
            public int compare(JobInstance jobInstance, JobInstance t1) {
                return jobInstance.getVersion().compareTo(t1.getVersion());
            }
        });
        List<JobExecution> jexe = jobExplorer.getJobExecutions(jobInstances.get(0));
        if (jexe == null || jexe.size()==0) {
            return null;
        }
        Collections.sort(jexe, new Comparator<JobExecution>() {
            @Override
            public int compare(JobExecution jobExecution, JobExecution t1) {
                return jobExecution.getStartTime().compareTo(t1.getStartTime());
            }
        });
        return jexe;
    }
}
