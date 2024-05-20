package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccp5.dto.Board;
import com.ccp5.dto.CommentDTO;

@Repository
public interface CommentRepository extends JpaRepository<CommentDTO, Long> {

	List<CommentDTO> findByBoard(Board board);

	List<CommentDTO> findByBoardNum(int bnum);
}
