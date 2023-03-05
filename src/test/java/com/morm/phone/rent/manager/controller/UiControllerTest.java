package com.morm.phone.rent.manager.controller;

import com.morm.phone.rent.manager.controller.web.UiController;
import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.dto.request.LoginRequest;
import com.morm.phone.rent.manager.dto.request.SignupRequest;
import com.morm.phone.rent.manager.dto.response.JwtResponse;
import com.morm.phone.rent.manager.security.JwtTokenUtil;
import com.morm.phone.rent.manager.service.JwtUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UiControllerTest {

  @Mock
  private JwtUserService jwtUserService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtTokenUtil jwtUtil;

  @Mock
  private PasswordEncoder encoder;

  @InjectMocks
  private UiController uiController;

  private final ModelMapper modelMapper = new ModelMapper();

  @Test
  void testSuccessfulRegistration() {
    // Given
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("testuser");
    signupRequest.setPassword("password123");
    signupRequest.setFirstName("John");
    signupRequest.setLastName("Doe");
    Employee employee = new Employee();
    modelMapper.map(signupRequest, employee, "password");
    String jwt = "test-jwt-token";

    Authentication authentication = mock(Authentication.class);
    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .willReturn(authentication);
    given(jwtUtil.generateJwtToken(any(Authentication.class))).willReturn(jwt);
    given(jwtUserService.existsByUsername(signupRequest.getUsername())).willReturn(false);
    given(encoder.encode(signupRequest.getPassword())).willReturn("password123");

    // When
    ResponseEntity<?> responseEntity = uiController.register(signupRequest);

    // Then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    JwtResponse response = (JwtResponse) responseEntity.getBody();
    assertEquals(jwt, response.getToken());
    verify(jwtUserService, Mockito.atMostOnce()).save(employee);
  }
  @Test
  void testRegistrationFailure() {
    // Given
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("testuser");
    signupRequest.setPassword("password123");
    signupRequest.setFirstName("John");
    signupRequest.setLastName("Doe");

    given(jwtUserService.existsByUsername(signupRequest.getUsername())).willReturn(true);

    // When
    ResponseEntity<?> responseEntity = uiController.register(signupRequest);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals("Error: Username is already taken!", responseEntity.getBody());
    verify(jwtUserService, Mockito.never()).save(any(Employee.class));
  }

  @Test
  void testLogin() {
    // Given
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("testuser");
    loginRequest.setPassword("password123");

    Authentication authentication = mock(Authentication.class);
    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .willReturn(authentication);
    given(jwtUtil.generateJwtToken(any(Authentication.class))).willReturn("test-jwt-token");

    // When
    ResponseEntity<?> responseEntity = uiController.login(loginRequest);

    // Then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    JwtResponse response = (JwtResponse) responseEntity.getBody();
    assertEquals("test-jwt-token", response.getToken());
  }

  @Test
  void testFailedLogin() {
    // Given
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("testuser");
    loginRequest.setPassword("password123");

    given(authenticationManager.authenticate(
        ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
        .willThrow(new BadCredentialsException("Invalid username or password"));

    // When
    try {
      ResponseEntity<?> responseEntity = uiController.login(loginRequest);
    } catch (Exception e) {
      // Then
      assertEquals("Invalid username or password", e.getMessage());
    }
  }
}
