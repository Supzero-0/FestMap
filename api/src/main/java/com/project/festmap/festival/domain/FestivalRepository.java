package com.project.festmap.festival.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long> {

  @Query("select f from Festival f join fetch f.address")
  List<Festival> findAllWithAddress();
}
