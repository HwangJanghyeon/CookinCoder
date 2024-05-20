package com.ccp5.config;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // HttpSession 생성 및 사용자 정보를 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("USER", userDetails);
        
        // 로그인 성공 후 리다이렉트할 경로 설정
        String targetUrl = determineTargetUrl(authentication);
        
        // 리다이렉트
        response.sendRedirect(targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        // 사용자 권한에 따라 다른 페이지로 리다이렉트할 수 있는 로직 구현
        // 여기서는 단순화를 위해 모든 사용자를 "/index"로 리다이렉트
        return "/index"; 
    }
}
