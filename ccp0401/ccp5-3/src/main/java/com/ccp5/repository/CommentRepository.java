package com.ccp5.repository;

import com.ccp5.dto.Comment;
import com.ccp5.dto.BoardDTO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByBoard(BoardDTO board);
}
