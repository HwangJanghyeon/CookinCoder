package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccp5.dto.Favorite;
import com.ccp5.dto.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
}