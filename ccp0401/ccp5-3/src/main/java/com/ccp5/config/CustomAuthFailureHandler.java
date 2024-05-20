package com.ccp5.config;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password. Please try again.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "Internal system error occurred. Please contact the administrator.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Account not found. Please sign up and log in.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "Authentication request denied. Please contact the administrator.";
        } else {
            errorMessage = "Login failed due to an unknown reason. Please contact the administrator.";
        }
        setDefaultFailureUrl("/member/login?error=true&exception=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
