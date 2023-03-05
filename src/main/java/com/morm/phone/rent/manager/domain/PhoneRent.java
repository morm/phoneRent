package com.morm.phone.rent.manager.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(PhoneRentId.class)
@Table(name = "phone_employee")
public class PhoneRent {

  @Id
  @ManyToOne
  private Employee employee;
  @Id
  @ManyToOne
  private Phone phone;
  @Column(name = "booked_at")
  private LocalDateTime bookedAt;
  @Column(name = "released_at")
  private LocalDateTime releasedAt;
  @Transient
  private Boolean isAvailable;

  public PhoneRent(Phone phone, Employee employee, LocalDateTime bookedAt,
      LocalDateTime releasedAt) {

    this.phone = phone;
    this.employee = employee;
    this.bookedAt = bookedAt;
    this.isAvailable = bookedAt == null || releasedAt != null;
  }
}
