package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
