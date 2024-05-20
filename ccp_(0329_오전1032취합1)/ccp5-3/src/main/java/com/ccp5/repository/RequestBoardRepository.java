package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ccp5.dto.RequestBoard;


@Repository
public interface RequestBoardRepository extends JpaRepository<RequestBoard, Integer> {

	RequestBoard findByNum(int num);

	@Modifying
	@Query("DELETE FROM RequestBoard rb WHERE rb.num = :num")
	void deleteByNum(@Param("num") int num);
	

}
