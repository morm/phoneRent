package com.morm.phone.rent.manager.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneRentItem {

  private Integer employeeId;
  private String employeeName;
  private Integer phoneId;
  private String phoneName;
  private LocalDateTime bookedAt;
  private Boolean isAvailable;
  private Boolean isEditable = true;
}
