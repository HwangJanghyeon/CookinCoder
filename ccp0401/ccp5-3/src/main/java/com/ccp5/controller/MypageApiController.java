package com.ccp5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.MypageDTO;
import com.ccp5.service.MypageService;

@RestController
@RequestMapping("/api/mypage")
public class MypageApiController {

    @Autowired
    private MypageService mypageService;

    // 전체 마이페이지 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<MypageDTO> getMypageInfo(@PathVariable Long userId) {
        MypageDTO mypageInfo = mypageService.getMypageInfo(userId);
        return ResponseEntity.ok(mypageInfo);
    }

    // 사용자가 작성한 게시글 목록 조회
    @GetMapping("/{userId}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(mypageService.getUserPosts(userId));
    }

    // 사용자가 찜한 게시글 목록 조회
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<?> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(mypageService.getUserFavorites(userId));
    }

    // 결제 요청이 온 게시글 목록 조회
    @GetMapping("/{userId}/payment-requests")
    public ResponseEntity<?> getPaymentRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(mypageService.getPaymentRequests(userId));
    }
}
