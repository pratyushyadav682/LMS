//package com.cdac.backend.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    // Define the SecurityFilterChain to configure HttpSecurity
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeHttpRequests()
//            .requestMatchers("/api/auth/signup",
//            		"/api/auth/login",
//            		"/api/courses" ,
//            		"/api/feedback",
//            		"/api/modules",
//            		"/api/courses/{courseId}/modules",
//            		"/api/courses/{courseId}").permitAll()  // Correct path
//            .anyRequest().authenticated()  // Require authentication for other requests
//            .and()
//            .formLogin().disable(); // Optional: Disable form login if you're using REST API
//
//        return http.build();
//    }
//
//    // Password encoder bean
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
package com.cdac.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Publicly accessible endpoints
            		.requestMatchers("/api/auth/signup",
                    		"/api/auth/login",
                    		"/api/courses" ,
                    		"/api/feedback",
                    		"/api/modules/{courseId}",
                    		"/api/auth/users",
                    		"/api/auth/users/{id}",
                    		"/api/courses/{courseId}/modules/{moduleId}" ,                    		"/api/courses/{courseId}/modules",
                    		"/api/courses/{courseId}").permitAll()  // Correct path

                // Endpoints requiring authentication
                .requestMatchers(HttpMethod.GET, "/api/auth/logged-in-users").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()

                // Admin-only access
                .requestMatchers(HttpMethod.GET, "/api/auth/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/auth/users/{userId}").hasRole("ADMIN")

                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
