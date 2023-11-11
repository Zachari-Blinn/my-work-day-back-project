package com.blinnproject.myworkdayback.repository;

import java.util.Optional;

import com.blinnproject.myworkdayback.model.ERole;
import com.blinnproject.myworkdayback.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}