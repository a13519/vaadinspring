package net.zousys.gba.function.batch.service;

import net.zousys.gba.function.batch.dto.JobDTO;
import net.zousys.gba.function.batch.entity.JobDetails;
import net.zousys.gba.function.batch.repository.JobDetailsRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BatchService {
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private JobLauncher asyncJobLauncher;
    @Autowired
    private Job aJob;
    @Autowired
    private Job bJob;
    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    /**
     *
     * @param name
     * @return
     */
    private boolean isRunning(String name) {
        JobInstance jobInstance = jobExplorer.getLastJobInstance(name);
        if (jobInstance == null) {
            return false;
        }
        JobExecution jobExecution = jobExplorer.getJobExecution(jobInstance.getId());
        return jobExecution.isRunning();
    }

    /**
     *
     * @param para
     */
    public Long runJobA(String para) {
        try {
            if (!isRunning("BatchJob_A")) {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("ENV", para)
                        .addString("Created", "" + System.currentTimeMillis())
                        .toJobParameters();
                JobInstance jobInstance = jobExplorer.getJobInstance(aJob.getName(), jobParameters);
                JobExecution jobExecution = asyncJobLauncher.run(aJob, jobParameters);
                return jobExecution.getId();
            } else {
                System.out.println("Job BatchJob_A is running");
                return 0L;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     *
     * @param para
     */
    public Long runJobB(String para) {
        try {
            if (!isRunning("BatchJob_B")) {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("ENV", para)
                        .addString("Created", "" + System.currentTimeMillis())
                        .toJobParameters();
                JobInstance jobInstance = jobExplorer.getJobInstance(bJob.getName(), jobParameters);
                JobExecution jobExecution = asyncJobLauncher.run(bJob, jobParameters);
                return jobExecution.getId();
            } else {
                System.out.println("Job BatchJob_B is running");
                return 0L;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     *
     * @param executionId
     * @return
     */
    public List<StepExecution> getStepExecutions(Long executionId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
        if (jobExecution != null) {
            return jobExecution.getStepExecutions().stream().toList();
        } else {
            return null;
        }
    }

    /**
     *
     * @param jobExecutionId
     * @param stepExecutionId
     * @return
     */
    public StepExecution getStepExecution(Long jobExecutionId, Long stepExecutionId) {
        return jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
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

    /**
     *
     * @param id
     * @return
     */
    public Optional<JobDTO> get(Long id) {
        JobExecution jobExecution = jobExplorer.getJobExecution(id);
        if (jobExecution != null) {
            Optional<JobDetails> jobDetails = jobDetailsRepository.findById(id);
            if (jobDetails.isPresent()) {
                return Optional.of(JobDTO.builder()
                        .id(jobExecution.getJobId())
                        .name(jobDetails.get().getName())
                        .status(jobDetails.get().getStatus())
                        .started(jobExecution.getStartTime())
                        .ended(jobExecution.getEndTime())
                        .log(jobDetails.get().getLog())
                        .build());
            }
        }
        return Optional.empty();
    }

    /**
     *
     * @param pageable
     * @return
     */
    public List<JobDTO> listAll(Pageable pageable) {
        Page<JobDetails> list = jobDetailsRepository.findAll(pageable);
        List<JobDTO> r = new ArrayList<>();
        List<JobDetails> details = list.stream().toList();

        for (JobDetails jobDetails : details) {
            r.add(jobDetails.convertToDTO());
        }
        return r;
    }

    /**
     *
     * @param name
     * @param pageable
     * @return
     */
    public List<JobDTO> listByName(String name, Pageable pageable) {
        Page<JobDetails> list = jobDetailsRepository.findByName(name, pageable);
        List<JobDTO> r = new ArrayList<>();
        List<JobDetails> details = list.stream().toList();

        for (JobDetails jobDetails : details) {
            r.add(jobDetails.convertToDTO());
        }
        return r;
    }


//    public Page<JobDTO> list(Pageable pageable, Specification<JobDTO> filter) {
//        return repository.findAll(filter, pageable);
//    }

    public int countEntities() {
        // Return the total count of entities
        return (int) jobDetailsRepository.count();
    }

    public int countEntities(String name) {
        // Return the total count of entities
        return (int) jobDetailsRepository.countByName(name);
    }
}
