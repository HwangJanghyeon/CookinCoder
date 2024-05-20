package com.ccp5.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ccp5.dto.DataDTO;


public interface DataRepository extends JpaRepository<DataDTO, Long> {
	@Query("SELECT DISTINCT d.category FROM DataDTO d")
    List<String> findDistinctCategories();
	
	@Query("SELECT d FROM DataDTO d WHERE d.category = :category")
    List<DataDTO> findByCategory(@Param("category") String category);
	
	List<DataDTO> findAll();
}
