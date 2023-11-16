package com.blinnproject.myworkdayback.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityAudit extends BaseEntity implements Serializable {
  @CreatedBy
  private String createdBy;

  @LastModifiedBy
  private String lastUpdatedBy;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private Instant createdOn;

  @UpdateTimestamp
  @Column(name = "last_updated_on")
  private Instant lastUpdatedOn;
}