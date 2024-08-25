package com.vrx.electronic.store.config;

import com.vrx.electronic.store.dto.UserDto;
import com.vrx.electronic.store.dto.response.JwtResponse;
import com.vrx.electronic.store.exception.BadApiRequest;
import com.vrx.electronic.store.security.JwtAuthenticationEntryPoint;
import com.vrx.electronic.store.security.JwtAuthenticationFilter;
import com.vrx.electronic.store.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // allows to restrict methods based on roles
public class SecurityConfig {


    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtHelper helper;

    private final String[] PUBLIC_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/configuration/**"
    };


    // declare the beans: spring will autoconfigure
    // Implementing JWT auth
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//
//        http.csrf(AbstractHttpConfigurer::disable)
////                .cors(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login").permitAll())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/google").permitAll())
//                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/users").permitAll())
//                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll())
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN");
//                    auth.anyRequest().authenticated();
//                })
////                .httpBasic(Customizer.withDefaults()); // used for basic authentication
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }


    //create password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    public JwtResponse doAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try {
            getAuthenticationManager(new AuthenticationConfiguration()).authenticate(authenticationToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String generatedToken = helper.generateToken(userDetails);
            return JwtResponse.builder()
                    .jwtToken(generatedToken)
                    .userDto(modelMapper.map(userDetails, UserDto.class)).build();
        } catch (BadCredentialsException b) {
            throw new BadApiRequest("Invalid Username or Password !!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //CORS configuration
    @Bean
    public CorsFilter corsFilter() {
        //create cors configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("https://domain2.com", "http://localhost:4200"));
        corsConfiguration.addAllowedHeader("Authorization");
        corsConfiguration.addAllowedHeader("Content-Type");
        corsConfiguration.addAllowedHeader("Accept");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("OPTION");
        source.registerCorsConfiguration("/**", corsConfiguration);
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CorsFilter corsFilter = new CorsFilter(source);
        filterRegistrationBean.setOrder(-5);
        return corsFilter;
    }
}
