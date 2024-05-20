package com.ccp5.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ccp5.dto.Role;
import com.ccp5.dto.User;
import com.ccp5.repository.UserRepository;


@Service
@Primary

public class UserService implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public void join(User user) {
        if (user.getRole() == null || user.getRole() == Role.USER) {
            user.setRole(Role.USER);
        } else if (user.getRole() == Role.ADMIN) {
            user.setRole(Role.ADMIN);
        }
        
        userRepository.save(user);
        logger.info("User saved: {}", user.getUsername());
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 사용자 역할 로깅
        logger.info("User loaded: {} with role: {}", username, user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            true, 
            true, 
            true, 
            true, 
            user.getAuthorities()
        );
    }

    }

