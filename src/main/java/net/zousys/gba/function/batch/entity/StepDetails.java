package net.zousys.gba.function.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import net.zousys.gba.function.batch.dto.StepDTO;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "step_details")
public class StepDetails extends AuditModel {

    @Id
    private Long id;
    private String name;
    private String status;
    private String parameters;
    private Long batchStepId;
    private String log;
    private String comments;
    private LocalDateTime started;
    private LocalDateTime ended;

    public StepDTO convertToDTO() {
        return StepDTO.builder()
                .id(getId())
                .name(getName())
                .status(getStatus())
                .started(getStarted())
                .ended(getEnded())
                .log(getLog())
                .parameters(getParameters())
                .build();
    }
}
