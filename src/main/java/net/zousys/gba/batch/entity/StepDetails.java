package net.zousys.gba.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "step_details")
public class StepDetails {

    @Id
    private Long id;
    private String name;
    private String status;
    private String parameters;
    private Long batchStepId;
    private String log;
    private String comments;

}
