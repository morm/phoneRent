package com.morm.phone.rent.manager.domain;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PhoneRentId implements Serializable {

  private Integer employee;
  private Integer phone;
}
