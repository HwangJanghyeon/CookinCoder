package com.ccp5.dto;

import java.util.List;

import lombok.Data;
@Data
public class MypageDTO {
	  private User user;
	 private List<Board> myBoards; // 사용자가 작성한 게시글
	    private List<Board> favoriteBoards; // 사용자가 찜한 게시글
	    private List<PaymentRequest> paymentRequests; // 결제 요청 목록
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public List<Board> getMyBoards() {
			return myBoards;
		}
		public void setMyBoards(List<Board> myBoards) {
			this.myBoards = myBoards;
		}
		public List<Board> getFavoriteBoards() {
			return favoriteBoards;
		}
		public void setFavoriteBoards(List<Board> favoriteBoards) {
			this.favoriteBoards = favoriteBoards;
		}
		public List<PaymentRequest> getPaymentRequests() {
			return paymentRequests;
		}
		public void setPaymentRequests(List<PaymentRequest> paymentRequests) {
			this.paymentRequests = paymentRequests;
		}
}
