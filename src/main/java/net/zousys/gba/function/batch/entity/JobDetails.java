package net.zousys.gba.function.batch.entity;

import jakarta.persistence.*;
import lombok.*;

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
