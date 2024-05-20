package com.ccp5.repository;

import com.ccp5.dto.CommentDTO;
import com.ccp5.dto.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
