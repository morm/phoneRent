package com.morm.phone.rent.manager.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "Username is required")
  @NotBlank(message = "Username must not be blank")
  @Size(max = 45, message = "Username cannot be longer than 100 characters")
  private String username;

  @NotNull(message = "Password is required")
  @NotEmpty(message = "Password must not be blank")
  @Size(max = 255, message = "Password cannot be longer than 100 characters")
  private String password;

  @Column(name = "first_name")
  @NotNull(message = "First name is required")
  @NotEmpty(message = "First name must not be blank")
  @Size(max = 45, message = "First name cannot be longer than 100 characters")
  private String firstName;
  @Column(name = "last_name")
  @NotNull(message = "Last name is required")
  @NotEmpty(message = "Last name must not be blank")
  @Size(max = 45, message = "Last name cannot be longer than 100 characters")
  private String lastName;
}
