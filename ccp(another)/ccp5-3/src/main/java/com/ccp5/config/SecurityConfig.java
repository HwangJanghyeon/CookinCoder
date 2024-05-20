package com.ccp5.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity

public class SecurityConfig {
	@Autowired
    private  CustomAuthenticationProvider customAuthenticationProvider;

  
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	  System.out.println("Configuring HttpSecurity");
        http
        .authorizeHttpRequests((auth) -> auth
        	    .requestMatchers("/", "/member/login", "/loginPro", "/member/join").permitAll()
        	    .requestMatchers("/reply/commentInsert/**").hasRole("USER")
        	    .requestMatchers("/admin/**").hasRole("ADMIN")
        	    .anyRequest().permitAll()
        	)
        	
            .formLogin((form) -> form
                .loginPage("/member/login")
                .loginProcessingUrl("/loginPro")
                .defaultSuccessUrl("/")
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/")
            )
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .csrf((csrf) -> csrf.disable());
        System.out.println("Finished configuring HttpSecurity");
        return http.build();
        
    }
   
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
            .ignoring()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/assets/**","/error");
    }

   
}
