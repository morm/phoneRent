package com.morm.phone.rent.manager.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

  private String username;
  private String password;
  private String firstName;
  private String lastName;
}
