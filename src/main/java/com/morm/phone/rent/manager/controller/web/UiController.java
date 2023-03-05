package com.morm.phone.rent.manager.controller.web;

import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.dto.request.LoginRequest;
import com.morm.phone.rent.manager.dto.request.SignupRequest;
import com.morm.phone.rent.manager.dto.response.JwtResponse;
import com.morm.phone.rent.manager.security.JwtTokenUtil;
import com.morm.phone.rent.manager.service.JwtUserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UiController {

  private final JwtUserService jwtUserService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtUtil;
  private final ModelMapper modelMapper;
  private final PasswordEncoder encoder;

  public UiController(
      AuthenticationManager authenticationManager,
      JwtUserService jwtUserService,
      JwtTokenUtil jwtUtil,
      PasswordEncoder encoder) {
    this.jwtUserService = jwtUserService;
    this.jwtUtil = jwtUtil;
    this.authenticationManager = authenticationManager;
    this.encoder = encoder;
    this.modelMapper = new ModelMapper();
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/register")
  public String register() {
    return "registration";
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtil.generateJwtToken(authentication);

    return ResponseEntity.ok(new JwtResponse(jwt));
  }

  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<?> register(@RequestBody SignupRequest signUpRequest) {
    if (jwtUserService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body("Error: Username is already taken!");
    }

    // Create new user's account
    Employee user = new Employee();
    modelMapper.map(signUpRequest, user, "password");
    user.setPassword(encoder.encode(signUpRequest.getPassword()));

    jwtUserService.save(user);

    return login(modelMapper.map(signUpRequest, LoginRequest.class));
  }
}
