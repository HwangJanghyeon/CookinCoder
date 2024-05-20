package com.ccp5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ccp5.dto.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getById(Long id);
    @Modifying
    @Query(value = "INSERT INTO tbl_user4 (username, name, password, email) VALUES (:username, :name, :password, :email)", nativeQuery = true)
    @Transactional
    void saveUser(@Param("username") String username, @Param("name") String name, @Param("password") String password, @Param("email") String email);

}
