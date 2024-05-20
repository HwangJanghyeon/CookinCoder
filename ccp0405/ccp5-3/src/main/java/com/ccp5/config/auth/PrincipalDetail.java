package com.ccp5.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // 추가
import org.springframework.stereotype.Service;

import com.ccp5.dto.User;
import com.ccp5.repository.UserRepository;

@Service
@Lazy
public class PrincipalDetail implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 추가

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username); // 사용자를 찾지 못했을 때 예외 처리
        }
        return new PrincipalUser(user);
    }
}
