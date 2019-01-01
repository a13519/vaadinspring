package net.zousys.gba.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class JobDetails {

    @Id
    private Long id;
    private String name;
    private String status;
    private String parameters;
    private Long batchJobExeId;
    private String log;
    private String comments;


}
