package com.morm.phone.rent.manager.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.morm.phone.rent.manager.controller.rest.PhonesController;
import com.morm.phone.rent.manager.dto.response.PhonesRentResponse;
import com.morm.phone.rent.manager.service.PhonesRentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class PhonesControllerTest {

  @Mock
  private PhonesRentService phonesRentService;

  @InjectMocks
  private PhonesController phonesController;

  @Test
  public void getRentedPhones_shouldReturnOk() {
    // Given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
    Principal principal = new UsernamePasswordAuthenticationToken(userDetails, "password");

    when(phonesRentService.getRentedPhones()).thenReturn(Collections.emptyList());

    // When
    ResponseEntity<PhonesRentResponse> result = phonesController.getRentedPhones(principal);

    // Then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getPhonesRent()).isNotNull().isEmpty();
  }

  @Test
  public void releasePhone_shouldReturnOk() {
    // Given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
    Principal principal = new UsernamePasswordAuthenticationToken(userDetails, "password");

    // When
    ResponseEntity<?> result = phonesController.releasePhone(false, principal, 1);

    // Then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
