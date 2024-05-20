package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ccp5.dto.IngrBoard;

import jakarta.transaction.Transactional;


public interface IngrListRepository extends JpaRepository<IngrBoard, Integer> {
	List<IngrBoard> findByTitle(String title);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM IngrBoard i WHERE i.title = ?1")
    void deleteByTitle(String title);
		
	@Transactional
	@Modifying
	@Query("DELETE FROM IngrBoard i WHERE i.unit IS NULL")
	void deleteByNull();

}
