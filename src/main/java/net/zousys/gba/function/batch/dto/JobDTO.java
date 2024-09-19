package net.zousys.gba.function.batch.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class JobDTO {
    private Long id;
    private Long jobId;
    private String name;
    private String status;
    private String parameters;
    private String log;
    private LocalDateTime started;
    private LocalDateTime ended;
}
