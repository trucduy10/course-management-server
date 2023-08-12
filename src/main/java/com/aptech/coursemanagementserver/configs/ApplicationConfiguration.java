package com.aptech.coursemanagementserver.configs;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_SANDBOX_MODE;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    @Bean
    // https://modelmapper.org/user-manual/configuration/
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        // modelMapper.getConfiguration().setFieldMatchingEnabled(true)
        // .setFieldAccessLevel(PRIVATE)
        // .setSourceNamingConvention(JAVABEANS_MUTATOR); // JAVABEAN Convention:
        // // https://docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm
        return modelMapper;
    }

    // @Bean
    // public ObjectMapper objectMapper() {
    // ObjectMapper objectMapper = new ObjectMapper();
    // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    // return objectMapper;
    // }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // get Email from our User (the lambda will return a UserDetails -> User
        // implement UserDetails)
        // @Override
        // public UserDetails loadUserByUsername(String email) throws
        // UsernameNotFoundException {
        // return userRepository.findByEmail(email)
        // .orElseThrow(() -> new UsernameNotFoundException("Invalid username or
        // password"));
        // }
        return (email) -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
    }

    @Bean
    // AuthenticationProvider is a Data Access Object (DAO) which is responsible to
    // fetch userDetails, encode password, ...
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Tell this DAOProvider which user details
                                                                            // which userDetailsService to use to fetch
                                                                            // information of user
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    // AuthenticationManager use to process an Authentication request.
    // AuthenticationConfiguration exports the authentication Configuration
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, PAYPAL_SANDBOX_MODE);
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        String accessToken = "FJLKDSJFLDSKJFLDS";
        try {
            accessToken = oAuthTokenCredential().getAccessToken();
        } catch (Exception e) {
            log.error("Get paypal accesstoken error:", e);
        }

        APIContext context = new APIContext(accessToken);
        context.setConfigurationMap(PAYPAL_SANDBOX_MODE);
        return context;
    }

    // @Autowired
    // JdbcTemplate jdbcTemplate;

    // @Bean
    // @Scope("prototype")
    // public SimpleJdbcCall getSimpleJdbcCall() {

    // return new SimpleJdbcCall(jdbcTemplate);

    // }
}
