package com.example.letsplay.security;

import com.example.letsplay.services.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // JWT filter for validating tokens in requests.
    // This filter intercepts HTTP requests to extract and validate JWT (JSON Web Tokens)
    // from the Authorization header. JWTs are used for securely transmitting information
    // between parties as a JSON object, and in the context of web security, they are
    // commonly used for user authentication and authorization.

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Handler for authentication errors
    // It is invoked when an unauthenticated user attempts to access a resource that requires authentication,
    // or when authentication fails. The JwtAuthenticationEntryPoint is responsible for sending the appropriate
    // HTTP response in such scenarios, typically a 401 Unauthorized status. This ensures that the client is
    // properly informed about the need for authentication, and it forms a critical part of the security
    // infrastructure, maintaining robust access control in the application.

    @Autowired
    private CorsHandler corsHandler; // CORS configuration handler
    // CORS is a security feature in web applications that restricts how resources can be accessed from different
    // domain origins. The CorsHandler defines the rules and policies, such as which domains are allowed to access
    // resources, what HTTP methods are permitted, and whether credentials can be included in cross-origin requests.
    // This is crucial for controlling how external web pages interact with your application's API, ensuring that
    // only authorized cross-origin requests are allowed, thereby enhancing the security and integrity of the application.


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Configure HTTP security
        return httpSecurity.csrf(csrf -> csrf.disable()) // Disable CSRF protection (common in REST APIs)
                .cors(cors -> cors.configurationSource(corsHandler)) // Apply CORS configuration
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless sessions
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Handle authentication exceptions
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/users/login", "/api/users/register").anonymous() // Allow anonymous access to login and register
                                .requestMatchers("/api/products/allProducts", "/api/products/allProducts/**", "/api/products/productDetails/**").permitAll()
                                .anyRequest().authenticated() // Require authentication for all other requests

                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build(); // Add JWT filter before processing requests
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // Configure the authentication manager
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Set custom user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
        return new ProviderManager(authProvider); // Return the configured authentication manager
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsImpl();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Provide the password encoder (BCrypt) that automatically hashes and salts passwords
        return new BCryptPasswordEncoder();
    }
}
