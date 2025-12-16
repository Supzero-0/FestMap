package com.project.festmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.festmap.domain.Festival;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long> {}
