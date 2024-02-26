package com.vrx.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    //declare the beans: spring will autoconfigure

    //configure user
    @Bean
    public UserDetailsService userDetailsService() {
        //create user
        UserDetails normal = User.builder() //org.springframework.security.core
                .username("Vandana")
                .password(passwordEncoder().encode("vandana123"))
                .roles("NORMAL")
                .build();

        UserDetails admin = User.builder()
                .username("Vijay")
                .password(passwordEncoder().encode("vijay123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager();
    }

    //create password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
