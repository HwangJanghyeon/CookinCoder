package com.ccp5.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ccp5.dto.User;
import com.ccp5.dto.User2;

import com.ccp5.repository.UserRepository2;

@Service
@Primary
public class UserService2 implements UserDetailsService {

    private final UserRepository2	 userRepository2;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService2(UserRepository2 userRepository2) {
        this.userRepository2 = userRepository2;
    }


    public User2 findByUsername(String username) {
        return userRepository2.findByUsername(username);
    }

    public void join(User2 user) {
        userRepository2.save(user);
        logger.info("User saved: {}", user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User2 user = userRepository2.findByUsername(username);
        if (user == null) {
            logger.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 모든 사용자를 USER 역할로 설정
        logger.info("User loaded: {} with role: USER", username);

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            true, 
            true, 
            true, 
            true, 
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) // 모든 사용자를 USER 역할로 설정
        );
    }



}
