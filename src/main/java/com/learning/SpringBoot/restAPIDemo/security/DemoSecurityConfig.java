package com.learning.SpringBoot.restAPIDemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

  // Add support for JDBC .. no need to write hard code
  @Bean
  public UserDetailsManager userDetailsManager(DataSource dataSource) {
    // // will use predefined schema Users and Authorities (Roles) table
    // return new JdbcUserDetailsManager(dataSource);

    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

    // define query to retrieve a user by username
    // Question mark is a placeholder for the username which will be passed in as a
    // parameter
    jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id = ?");

    // define query to retrieve the authority (role) for a user by username
    jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
        "select user_id, role from roles where user_id = ?");

    return jdbcUserDetailsManager;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(configurer -> configurer
        .requestMatchers(HttpMethod.GET, "magic-api/employees").hasRole("EMPLOYEE")
        .requestMatchers(HttpMethod.GET, "magic-api/employees/**").hasRole("EMPLOYEE")
        .requestMatchers(HttpMethod.POST, "magic-api/employees").hasRole("MANAGER")
        .requestMatchers(HttpMethod.PUT, "magic-api/employees").hasRole("MANAGER")
        .requestMatchers(HttpMethod.DELETE, "magic-api/employees/**").hasRole("ADMIN"));

    // use HTTP Basic authentication
    http.httpBasic(Customizer.withDefaults());

    // disable CSRF
    http.csrf(csrf -> csrf.disable());

    return http.build();
  }

  // @Bean
  // public InMemoryUserDetailsManager userDetailManager() {
  // UserDetails john = User.builder()
  // .username("john")
  // .password("{noop}test123")
  // .roles("EMPLOYEE")
  // .build();

  // UserDetails mary = User.builder()
  // .username("mary")
  // .password("{noop}test123")
  // .roles("EMPLOYEE", "MANAGER")
  // .build();

  // UserDetails susan = User.builder()
  // .username("susan")
  // .password("{noop}test123")
  // .roles("EMPLOYEE", "Manager", "ADMIN")
  // .build();

  // return new InMemoryUserDetailsManager(john, mary, susan);
  // }

}
