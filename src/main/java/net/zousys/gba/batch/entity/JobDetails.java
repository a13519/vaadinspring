package net.zousys.gba.batch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Auditable;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

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
    private Long batchJobExeId;
    private String log;
    private String comments;

}
