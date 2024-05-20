package com.ccp5.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccp5.dto.RequestBoard;
import com.ccp5.repository.RequestBoardRepository;

import jakarta.transaction.Transactional;

@Service

public class RequestBoardSevice {
	@Autowired
	private  RequestBoardRepository rbRepo;
	
	// 요청 게시글 추가
	public void insert(RequestBoard rboard) {
		rbRepo.save(rboard);
	}
	
	// 요청 게시글 목록 불러오기
	public List<RequestBoard> list(){
		return rbRepo.findAll();
	}
	
	// 요청 게시글 자세히 보기
	public RequestBoard findByNum(int num) {
		return rbRepo.findByNum(num);
	}
	
	// 요청 게시글 업데이트
	@Transactional
	public RequestBoard update(RequestBoard rBoard) {
		RequestBoard rb = rbRepo.findByNum(rBoard.getNum());
		rb.setTitle(rBoard.getTitle());
		rb.setWriter(rBoard.getWriter());
		rb.setContent(rBoard.getContent());
		return rbRepo.save(rb);
		
	}
	// 요청 게시글 삭제
	@Transactional
	public void delete(int num) {
		rbRepo.deleteByNum(num);
	}
}
