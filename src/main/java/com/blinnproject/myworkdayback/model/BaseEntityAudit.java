package com.blinnproject.myworkdayback.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntityAudit extends BaseEntity implements Serializable {
    private String createdBy;
    private String lastUpdatedBy;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "last_updated_on")
    private Instant lastUpdatedOn;
}
