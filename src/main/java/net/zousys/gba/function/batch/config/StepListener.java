package net.zousys.gba.function.batch.config;

import net.zousys.gba.function.batch.entity.StepDetails;
import net.zousys.gba.function.batch.repository.StepDetailsRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StepListener implements StepExecutionListener {

    @Autowired
    private StepDetailsRepository stepDetailsRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepDetails stepDetails = StepDetails.builder()
                .id(stepExecution.getId())
                .name(stepExecution.getStepName())
                .log("ccc")
                .status(stepExecution.getStatus().toString())
                .batchStepId(stepExecution.getId())
                .started(stepExecution.getStartTime())
                .build();
        stepDetailsRepository.save(stepDetails);
        System.out.println("Before Step: " + stepExecution.getStepName());
        // Custom logic before the step execution
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        Optional<StepDetails> ostepDetails = stepDetailsRepository.findById(stepExecution.getId());
        StepDetails stepDetails = ostepDetails.get();
        if (stepDetails != null) {
            stepDetails.setStatus(stepExecution.getStatus().toString());
            stepDetails.setEnded(stepExecution.getEndTime());
            stepDetailsRepository.save(stepDetails);
        }
        return stepExecution.getExitStatus(); // Return the same or modify ExitStatus
    }
}