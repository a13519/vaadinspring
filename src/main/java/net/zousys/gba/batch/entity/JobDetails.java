package net.zousys.gba.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_details")
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
