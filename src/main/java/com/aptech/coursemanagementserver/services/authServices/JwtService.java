package com.aptech.coursemanagementserver.services.authServices;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.enums.TokenType;
import com.aptech.coursemanagementserver.models.Token;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/*
 * JWT Service manipulate Jwt Token
 */

@Slf4j
@Service
public class JwtService {
  @Autowired
  private TokenRepository tokenRepository;

  @Value("${application.security.jwt.tokenSecret}")
  private String secretKey;
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;
  @Value("${application.security.jwt.refreshToken.expiration}")
  private long refreshExpiration;

  // Method receive token and extract to get email of user
  public String extractUsername(String token) {
    try {
      return extractClaim(token, Claims::getSubject);// Subject should be username (email) of User

    } catch (Exception e) {
      return null;
      // throw new InvalidTokenException(INVALID_TOKEN_EXCEPTION,
      // HttpStatus.FORBIDDEN);
    }
  }

  // Method to extract Claim that we pass
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // Method to generate Token NOT INCLUDE EXTRA CLAIMS
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    try {

      List<String> authorities = new ArrayList<String>();
      userDetails.getAuthorities().stream().forEach(auth -> authorities.add(auth.getAuthority()));
      extraClaims.put("roles", authorities);
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    return generateToken(extraClaims, userDetails);
  }

  // Method to generate Token take a Map<String, Object> with EXTRA CLAIMS that we
  // want to add
  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(
      UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      long expiration) {
    return Jwts
        .builder()
        .setClaims(extraClaims) // Our new claims
        .setSubject(userDetails.getUsername()) // Subject should be username (email) of user
        .setIssuedAt(new Date(System.currentTimeMillis())) // This claim to create the time instant to calculate expired
        .setExpiration(new Date(System.currentTimeMillis() + expiration)) // This is the time token expired

        .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Get our secret key and pass the signature algorithm to
                                                            // encrypt our key
        .compact(); // Compact() will generate and return the Token
  }

  // Method to validate Token -> check if the token belong to the UserDetail and
  // Token is not expired
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  // Method to check if Token is expired (if exp_date < today) true
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);// Get the exp from our claims
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey()) // When generate or decode a token we need a Signing Key - a secret key that
                                       // used to digitally sign the JWT, create the Signature part of JWT
        .build()
        .parseClaimsJws(token) // To parse the token
        .getBody(); // Get all claims in this token
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // Save the Token of User to database
  public void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .token_type(TokenType.BEARER_ACCESS_TOKEN)
        .isExpired(false)
        .isRevoked(false)
        .build();
    tokenRepository.save(token);
  }

  // Overload
  public String saveUserToken(User user) {
    String accesstoken = generateToken(user);
    var token = Token.builder()
        .user(user)
        .token(accesstoken)
        .token_type(TokenType.BEARER_ACCESS_TOKEN)
        .isExpired(false)
        .isRevoked(false)
        .build();
    tokenRepository.save(token);
    return accesstoken;
  }

  public String saveUserRefreshToken(User user) {
    String refresh = generateRefreshToken(user);
    var token = Token.builder()
        .user(user)
        .token(refresh)
        .token_type(TokenType.BEARER_REFRESH_TOKEN)
        .isExpired(false)
        .isRevoked(false)
        .build();
    tokenRepository.save(token);
    return refresh;
  }

  public void saveUserVerificationToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .token_type(TokenType.VERIFY)
        .isExpired(false)
        .isRevoked(false)
        .build();
    tokenRepository.save(token);
  }

  public void saveUserResetPasswordToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .token_type(TokenType.RESET_PASSWORD)
        .isExpired(false)
        .isRevoked(false)
        .build();
    tokenRepository.save(token);
  }

}
