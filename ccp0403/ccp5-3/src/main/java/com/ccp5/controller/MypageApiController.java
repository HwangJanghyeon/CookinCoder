package com.ccp5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.Board;
import com.ccp5.dto.MypageDTO;
import com.ccp5.service.MypageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/mypage")
public class MypageApiController {

    @Autowired
    private MypageService mypageService;

    // ObjectMapper 인스턴스 생성
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 전체 마이페이지 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<String> getMypageInfo(@PathVariable Long userId) {
        try {
            MypageDTO mypageInfo = mypageService.getMypageInfo(userId);
            // MypageDTO 객체를 JSON 문자열로 변환
            String jsonResponse = objectMapper.writeValueAsString(mypageInfo);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            // JSON 변환 과정에서 예외 발생 시, 오류 메시지와 함께 500 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON processing error");
        }
    }

    @GetMapping("/posts/{username}")
    public ResponseEntity<String> getUserPostsByUsername(@PathVariable String username) {
        try {
            List<Board> posts = mypageService.getUserPostsByUsername(username);
            // ObjectMapper를 사용하여 LocalDateTime을 올바르게 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonResponse = objectMapper.writeValueAsString(posts);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON processing error");
        }
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
