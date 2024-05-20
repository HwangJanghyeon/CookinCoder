package com.ccp5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.Board;
import com.ccp5.dto.CommentDTO;
import com.ccp5.dto.User;
import com.ccp5.repository.BoardRepository;
import com.ccp5.repository.UserRepository;
import com.ccp5.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentApiController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private UserRepository userRepository;

	// 댓글창 출력
	@GetMapping
	public ResponseEntity<List<CommentDTO>> getAllComments(@RequestParam("boardNum") int boardNum) {
		List<CommentDTO> comments = commentService.findCommentsByBnum(boardNum);
		for (CommentDTO elements:comments) {
			System.out.println("댓글 작성자 ID 목록 : "+elements.getWriter().getId());
		}
		if (!comments.isEmpty()) {
			return ResponseEntity.ok(comments);
		} else {
			return ResponseEntity.notFound().build();
		}
	}


	//댓글작성
	@PostMapping
	public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO comment,
	        @RequestParam("boardNum") int boardNum, @RequestParam("username") String username) {
	    Board board = boardRepository.findByNum(boardNum);
	    User user = userRepository.findByUsername(username);
	    if (board == null || user == null) {
	        return ResponseEntity.notFound().build();
	    }
	    comment.setBoard(board);
	    comment.setWriter(user);
	    comment.setUsername(user.getName()); // 유저의 이름을 CommentDTO에 설정
	    commentService.createComment(comment);
	    return ResponseEntity.status(HttpStatus.CREATED).body(comment);
	}


	// 댓글 수정
	@PutMapping("/{cnum}")
	public ResponseEntity<CommentDTO> updateComment(@PathVariable Long cnum, @RequestBody CommentDTO updatedComment) {
		CommentDTO comment = commentService.getComment(cnum);
		if (comment != null) {
			commentService.updateComment(cnum, updatedComment);
			return ResponseEntity.ok(updatedComment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 댓글 삭제
	@DeleteMapping("/{cnum}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long cnum) {
		CommentDTO comment = commentService.getComment(cnum);
		if (comment != null) {
			commentService.deleteComment(cnum);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}