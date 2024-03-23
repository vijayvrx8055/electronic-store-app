package com.vrx.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
        //Authorization
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header:{}", requestHeader);
        //Bearer 2353453skdgfhjsdhfjsdhfg

        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
//            a. Get token from request
            token = requestHeader.substring(7);
            logger.info("token:{}", token);
            try {
//                b. Get username from token
                username = this.jwtHelper.getUsernameFromToken(token);
                logger.info("username:{}", username);
            } catch (IllegalArgumentException e) {
                logger.error("Illegal Argument while fetching the username!");
            } catch (ExpiredJwtException e) {
                logger.error("Given JWT token is expired!!");
            } catch (MalformedJwtException e) {
                logger.info("Token has been tampered. Invalid Token!!");
            } catch (Exception e) {
                logger.error("Error occurred while creating token !!");
            }
        } else {
            logger.info("Invalid Header Value!!");
        }
        if (username != null && SecurityContextHolder.getContext() != null) {
//            c. Load user associated with token
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.info("UserDetails: {}", userDetails);
//            d. Validate token
            Boolean validatedToken = this.jwtHelper.validateToken(token, userDetails);
            if (validatedToken) {
                //e. set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.error("Token Validation Failed !!");
            }
        }
        filterChain.doFilter(request,response);
    }
}
