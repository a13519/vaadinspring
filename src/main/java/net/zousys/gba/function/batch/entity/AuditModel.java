package net.zousys.gba.function.batch.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIME)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @JsonIgnore
    @Setter
    @Getter
    private Date createdAt;

    @Temporal(TemporalType.TIME)
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    @JsonIgnore
    @Setter
    @Getter
    private Date updatedAt;

    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String authority;

}
