package com.project.festmap.user.domain;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query("SELECT f.id FROM User u JOIN u.favoriteFestivals f WHERE u.id = :userId")
  Set<Long> findFavoriteFestivalIdsByUserId(@Param("userId") UUID userId);
}
