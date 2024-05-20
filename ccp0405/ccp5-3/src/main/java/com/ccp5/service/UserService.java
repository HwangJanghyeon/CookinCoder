package com.ccp5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ccp5.dto.User;
import com.ccp5.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Primary
public class UserService /* implements UserDetailsService */ {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void join(User user) {
        userRepository.save(user);
        logger.info("User saved: {}", user.getUsername());
    }

   /* @Override
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
            user.getAuthorities()
        );*/
	/* } */
}
