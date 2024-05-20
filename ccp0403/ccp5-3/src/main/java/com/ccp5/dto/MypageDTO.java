package com.ccp5.dto;

import java.util.List;

import lombok.Data;
@Data
public class MypageDTO {
	  private User user;
	 private List<Board> myBoards; // 사용자가 작성한 게시글
	    private List<Board> favoriteBoards; // 사용자가 찜한 게시글
	    private List<PaymentRequest> paymentRequests; // 결제 요청 목록
}
