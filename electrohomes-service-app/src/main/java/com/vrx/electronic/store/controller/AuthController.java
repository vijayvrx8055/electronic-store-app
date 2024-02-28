package com.vrx.electronic.store.controller;

import com.vrx.electronic.store.config.SecurityConfig;
import com.vrx.electronic.store.dto.UserDto;
import com.vrx.electronic.store.dto.request.JwtRequest;
import com.vrx.electronic.store.dto.response.JwtResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        JwtResponse jwtResponse = this.securityConfig.doAuthentication(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);

    }

}
