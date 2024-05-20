package com.ccp5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ccp5.dto.BoardDTO;
import com.ccp5.dto.CommentDTO;
import com.ccp5.dto.User;
import com.ccp5.repository.BoardRepository;
import com.ccp5.repository.CommentRepository;
import com.ccp5.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BoardRepository boardRepository;

	// 모든 댓글 조회
	public List<CommentDTO> getAllComments() {
		return commentRepository.findAll();
	}
	
	// 특정 게시글 댓글 조회
	public List<CommentDTO> findCommentsByBnum(int bnum){
		return commentRepository.findByBoardNum(bnum);
	}

	// 댓글 작성
	public void createComment(CommentDTO comment) {
		// 컨텍스트 홀더로 인증객체 얻어서 사용자 정보 추출 후 연결
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		User user = userRepository.findByUsername(username);
		if (user != null) {
			comment.setWriter(user);
		}
		// 보드가 이미 저장되어 있거나 저장된 후에 댓글을 저장
		commentRepository.save(comment);
	}

	// 댓글 조회
	public CommentDTO getComment(Long cnum) {
		return commentRepository.findById(cnum).orElse(null);
	}

	// 댓글 수정
	public void updateComment(Long cnum, CommentDTO updatedComment) {
		CommentDTO comment = commentRepository.findById(cnum).orElse(null);
		if (comment != null) {
			// 수정할 내용 업데이트
			comment.setContent(updatedComment.getContent());
			// 기타 필요한 업데이트 작업 수행
			commentRepository.save(comment);
		}
	}

	// 댓글 삭제
	public void deleteComment(Long cnum) {
		commentRepository.deleteById(cnum);
	}
}