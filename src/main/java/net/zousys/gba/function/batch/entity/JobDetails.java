package net.zousys.gba.function.batch.entity;

import jakarta.persistence.*;
import lombok.*;
import net.zousys.gba.function.batch.dto.JobDTO;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_details")
public class JobDetails extends AuditModel {

    @Id
    private Long id;
    private String name;
    private String status;
    private String parameters;
    private Long batchJobId;
    private String log;
    private String comments;
    private LocalDateTime started;
    private LocalDateTime ended;

    public JobDTO convertToDTO() {
        return JobDTO.builder()
                .id(getId())
                .jobId(getBatchJobId())
                .name(getName())
                .status(getStatus())
                .started(getStarted())
                .ended(getEnded())
                .log(getLog())
                .parameters(getParameters())
                .build();
    }
}
