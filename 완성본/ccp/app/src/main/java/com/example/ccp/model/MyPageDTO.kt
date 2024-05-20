package com.example.ccp.model

data class MypageDTO(
    var user: User,
    var myBoards: List<BoardDTO>, // 사용자가 작성한 게시글
    var favoriteBoards: List<BoardDTO>, // 사용자가 찜한 게시글
    var paymentRequests: List<PaymentRequest> // 결제 요청 목록
)
