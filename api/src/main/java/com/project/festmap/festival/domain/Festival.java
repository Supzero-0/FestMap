package com.project.festmap.festival.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.project.festmap.address.domain.Address;
import com.project.festmap.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "festival")
public class Festival {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column private String description;

  @OneToOne
  @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
  private Address address;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(length = 100)
  private String genre;

  @ManyToMany(mappedBy = "favoriteFestivals")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<User> favoritedBy = new HashSet<>();
}
