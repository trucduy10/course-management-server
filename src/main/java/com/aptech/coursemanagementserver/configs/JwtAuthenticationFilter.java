package com.aptech.coursemanagementserver.configs;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.HEADER_STRING;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.TOKEN_PREFIX;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aptech.coursemanagementserver.repositories.TokenRepository;
import com.aptech.coursemanagementserver.services.authServices.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * JWTAuthenticationFilter workflow:
 * Step 1: Checking if we have JWT Token or not
 * Step 2: Call UserServicesDetail to check if we have user exist in database or not -> to do that we need to call JWTService to extract username
 * Step 3: If user is authenticated then update the SecurityContextHolder and send to DispatcherServlet
 */

//Everytime user send request. JwtAuthFilter will get fired
@Slf4j
@Component
@RequiredArgsConstructor // Create a constructor using all the final field declared
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequest will get fired one every request
                                                                    // sent

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  // intercept every request and extract data from every request and provide data
  // within the response
  protected void doFilterInternal(
      // NonNull to get rid of warning that these param should not null
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain // contain list of other filters
  ) throws ServletException, IOException {
    // if (request.getServletPath().contains("/auth")) {
    // filterChain.doFilter(request, response);// call next filter within chain
    // return;
    // }
    final String authHeader = request.getHeader(HEADER_STRING); // Header Authorization contains JWT Token (Bearer
                                                                // Token)
    final String jwt;
    final String userEmail;
    // Check if Header is null or not contain "Bearer " in Token
    if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
      filterChain.doFilter(request, response); // Continue to next filter
      return; // get out of doFilterInternal
    }
    jwt = authHeader.substring(7); // "Bearer " has 7 characters
    try {
      userEmail = jwtService.extractUsername(jwt); // Extract username (Email in this case) from Jwt Token

      // SecurityContextHolder.getContext().getAuthenticate() == null means User is
      // not authenticated
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // To make userDetails come from User from database call loadUserByUsername() in
        // userDetailsService that implements in AppConfiguration
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        var isTokenValid = tokenRepository.findByToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          // UsernamePasswordAuthenticationToken is the class help us update the
          // SecurityContextHolder
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
          // Set the details from our HTTP Request
          authToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          // Update SecurityContextHolder with authToken
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception e) {
      log.error("Request from {} with error {}", request.getRequestURL(), e.getMessage());

    } // "org.hibernate.LazyInitializationException: failed to lazily initialize a
      // collection of role:
      // com.aptech.coursemanagementserver.models.User.userPermissions: could not
      // initialize proxy - no Session"
    filterChain.doFilter(request, response);

  }
}
