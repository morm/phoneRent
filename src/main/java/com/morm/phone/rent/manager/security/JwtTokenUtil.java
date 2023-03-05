package com.morm.phone.rent.manager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
  @Value("${jwt.expiration: 86400}")
  Integer expiration;
  @Value("${jwt.secret}")
  private String secret;

  //retrieve username from jwt token
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  //retrieve expiration date from jwt token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  //for retrieveing any information from token we will need the secret key
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  //check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  //generate token for user
  public String generateJwtToken(Authentication authentication) {

    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

    Instant currentDate = Instant.now();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(java.util.Date.from(currentDate))
        .setExpiration(java.util.Date.from(currentDate.plus(Duration.ofSeconds(expiration))))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  //validate token
  public Boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
