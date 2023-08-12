package com.aptech.coursemanagementserver.configs;

import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_CREATE;
import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_DELETE;
import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_READ;
import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_UPDATE;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_CREATE;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_DELETE;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_READ;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_UPDATE;
import static com.aptech.coursemanagementserver.enums.Role.ADMIN;
import static com.aptech.coursemanagementserver.enums.Role.MANAGER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aptech.coursemanagementserver.events.handler.OAuth2AuthenticationFailureHandler;
import com.aptech.coursemanagementserver.events.handler.OAuth2AuthenticationSuccessHandler;
import com.aptech.coursemanagementserver.repositories.HttpCookieOAuth2AuthorizationRequestRepository;
import com.aptech.coursemanagementserver.services.authServices.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

        private final CustomOAuth2UserService customOAuth2UserService;

        private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

        private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutHandler logoutHandler;
        @Value("${application.security.cors.allowedOrigins}")
        private String[] allowedOrigins;
        public static final String[] ENDPOINTS_WHITELIST = {
                        "/home/**",
                        "/category/**",
                        "/author/**",
                        "/track/**",
                        "/push-notifications/**",
                        "/blog/**",
                        "/checkout/**",
                        "/stream/**",
                        "/post/**",
                        "/momo/**",
                        "/paypal/**",
                        "/video/**",
                        "/auth/**",
                        "/oauth2/**",
                        "/course/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"

        };

        @Bean
        public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
                return new HttpCookieOAuth2AuthorizationRequestRepository();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        /*
         * SecurityFilterChain to use our JwtFilterChain
         */

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.cors(withDefaults())
                                .csrf()
                                .disable()

                                .authorizeHttpRequests()
                                .requestMatchers("/subcribes/author/{authorId}").permitAll()
                                .requestMatchers(ENDPOINTS_WHITELIST)// requestMatchers(...String) If the
                                                                     // HandlerMappingIntrospector is
                                                                     // available
                                // in the classpath, maps to an MvcRequestMatcher that does not care which
                                // HttpMethod is used.
                                .permitAll() // Specify that URLs are allowed by anyone.

                                // hasAnyRole Specifies that a user requires one of many roles
                                // This mean the endpoint only accessible by user have the ADMIN or MANAGER role
                                .requestMatchers("/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())

                                // hasAnyAuthority Specifies that a user requires one of many authorities
                                // This mean the endpoint only accessible by user have the ADMIN_READ or
                                // MANAGER_READ privilege
                                .requestMatchers(GET,
                                                "/management/**")
                                .hasAnyAuthority(ADMIN_READ.name(),
                                                MANAGER_READ.name())

                                .requestMatchers(POST, "/management/**")
                                .hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/management/**")
                                .hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                .requestMatchers(DELETE, "/management/**")
                                .hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

                                .requestMatchers("/admin/**").hasRole(ADMIN.name())
                                .requestMatchers(GET, "/admin/**").hasAuthority(ADMIN_READ.name())
                                .requestMatchers(POST, "/admin/**").hasAuthority(ADMIN_CREATE.name())
                                .requestMatchers(PUT, "/admin/**").hasAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE,
                                                "/admin/**")
                                .hasAuthority(ADMIN_DELETE.name())

                                .anyRequest() // Maps any request.
                                .authenticated() // Attempts to authenticate the passed Authentication object, returning
                                                 // a fully
                                                 // populated Authentication object (including granted authorities) if
                                                 // successful.
                                // .and().exceptionHandling(handling -> handling
                                // .authenticationEntryPoint(
                                // new AuthenticationEntryPoint()))
                                .and()
                                .oauth2Login().loginPage("/auth/noauth")
                                .authorizationEndpoint()
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                                .and()
                                .redirectionEndpoint()
                                .baseUri("/oauth2/callback/*")
                                .and()
                                .userInfoEndpoint()
                                .userService(customOAuth2UserService)
                                .and()
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                                // .and()
                                // .sessionManagement()
                                // .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Spring will create
                                // new
                                // Session for each
                                // Request
                                .and()
                                .authenticationProvider(authenticationProvider)// authenticationProvider use
                                                                               // DAOAuthenticationProvider
                                                                               // from AppConfiguration
                                                                               // authenticationProvider() Allows adding
                                                                               // an additional
                                                                               // AuthenticationProvider to be used

                                // Add our JwtAuthFilter before UsernamePasswordAuthenticationFilter

                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                                // .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl() {
                                // @Override
                                // public void handle(HttpServletRequest request, HttpServletResponse response,
                                // org.springframework.security.access.AccessDeniedException
                                // accessDeniedException)
                                // throws IOException, ServletException, java.io.IOException {
                                // super.handle(request, response, accessDeniedException);

                                // log.info("Request: ", request);
                                // log.info("Response: ", response);
                                // log.info("error: ", accessDeniedException);

                                // }
                                // }).and()

                                .logout()
                                .logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder
                                                .clearContext());

                return http.build();
        }
}
