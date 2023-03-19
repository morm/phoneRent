package com.morm.phone.rent.manager.filter;

import static java.util.Optional.ofNullable;

import com.morm.phone.rent.manager.security.JwtTokenUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {

  private static final String BEARER_HEADER = "Bearer ";
  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
  private JwtTokenUtil jwtUtils;
  private UserDetailsService userDetailsService;

  public AuthTokenFilter(JwtTokenUtil jwtUtils, UserDetailsService userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      // validate token and set user authentication
      ofNullable(parseJwt(request)).ifPresent(jwt -> {
        jwtUtils.validateToken(jwt);
        String username = jwtUtils.getUsernameFromToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      });
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is invalid.");
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    // JWT token is passed in the request Authorization header: Bearer <token>
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_HEADER)) {
      return headerAuth.substring(BEARER_HEADER.length());
    }

    return null;
  }
}

