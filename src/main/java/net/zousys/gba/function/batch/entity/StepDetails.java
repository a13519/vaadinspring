package net.zousys.gba.function.batch.entity;

import jakarta.persistence.*;
import lombok.*;

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

}
