package net.zousys.gba.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JobDetails {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String status;
    private String parameters;
    private Integer batchJobId;
}
