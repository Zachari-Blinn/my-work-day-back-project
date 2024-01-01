package com.blinnproject.myworkdayback.model;

import com.blinnproject.myworkdayback.model.common.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exercise")
public class Exercise extends BaseEntityAudit {

  @Column(length = 150, nullable = false)
  private String name;

  @ElementCollection(targetClass = EMuscle.class)
  @CollectionTable(name = "exercise_muscle", joinColumns = @JoinColumn(name = "exercise_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "muscle_name")
  private Set <EMuscle> musclesUsed;
}
