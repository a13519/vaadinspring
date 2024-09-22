package net.zousys.gba.function.observable.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ObservDTO {
    private Long id;
    private String nativeId;
    private String name;
    private String status;
    private String parameters;
    private String log;
    private String branch;
    private String label;
    private LocalDateTime started;
    private LocalDateTime ended;
}
