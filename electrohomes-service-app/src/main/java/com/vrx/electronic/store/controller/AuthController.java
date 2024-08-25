package com.vrx.electronic.store.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vrx.electronic.store.config.SecurityConfig;
import com.vrx.electronic.store.dto.UserDto;
import com.vrx.electronic.store.dto.request.JwtRequest;
import com.vrx.electronic.store.dto.response.JwtResponse;
import com.vrx.electronic.store.entity.User;
import com.vrx.electronic.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
//@CrossOrigin("*") // Cross Origin Resource Sharing:-
// It is mechanism that allows a server to indicate any origin other than its own,
// from which a browser should permit loading the resources.
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.password}")
    private String googleClientPassword;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    // login with google api
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {
        // get the id token from request which is sent by google
        String idToken = data.get("idToken").toString();
        // below two classes are used to prepare verifier
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        // preparing the verifier
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier
                .Builder(netHttpTransport, jacksonFactory)
                .setAudience(Collections.singleton(googleClientId));
        // parsing string token to make GoogleIdToken
        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        logger.info("Payload: {}", payload);
        String email = payload.getEmail();

        User user = this.userService.getUserByEmailOptional(email).orElse(null);
        logger.info("User:{}",user);
        if (user == null) {
            logger.info("User is not registered! Creating new User!");
            this.userService.createNewUserByDefault(email, data.get("name").toString(),
                    (String) data.get("photoUrl").toString(), googleClientPassword);
            return this.login(JwtRequest.builder().email(email).password(googleClientPassword).build());
        } else {
            logger.info("Creating JWT Response!");
            return this.login(JwtRequest.builder().email(email).password(googleClientPassword).build());
        }
    }

}
