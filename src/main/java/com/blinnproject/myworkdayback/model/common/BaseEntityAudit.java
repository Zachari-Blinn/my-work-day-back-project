package com.blinnproject.myworkdayback.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityAudit extends BaseEntity implements Serializable {
  @Setter
  @CreatedBy
  private Long createdBy;

  @LastModifiedBy
  private Long lastUpdatedBy;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private Instant createdOn;

  @UpdateTimestamp
  @Column(name = "last_updated_on")
  private Instant lastUpdatedOn;
}