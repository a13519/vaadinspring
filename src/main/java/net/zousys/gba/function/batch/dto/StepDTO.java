package net.zousys.gba.function.batch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
public class StepDTO {
    private Long id;
    private String name;
    private String status;
    private String parameters;
    private Long batchStepId;
    private String log;
    private String comments;
    private LocalDateTime started;
    private LocalDateTime ended;

}
