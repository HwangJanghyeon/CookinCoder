package com.ccp5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.Board;
import com.ccp5.dto.Favorite;
import com.ccp5.dto.MypageDTO;
import com.ccp5.service.MypageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.EntityNotFoundException;

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


    @GetMapping("/{userId}/favorites")
    public ResponseEntity<?> getUserFavorites(@PathVariable Long userId) {
        try {
            List<Board> favoriteBoards = mypageService.getUserFavorites(userId);
            // ObjectMapper를 사용하여 List<Board>을 JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonResponse = objectMapper.writeValueAsString(favoriteBoards);
            return ResponseEntity.ok(jsonResponse);
        } catch (EntityNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            // 그 외의 예외 발생 시
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON processing error");
        }
    }



    // 클라이언트로부터 받은 찜하기 요청 처리
    @PostMapping("/favorites")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        try {
            // FavoriteRequest는 클라이언트로부터 username과 boardId를 받기 위한 DTO입니다.
            // favoriteRequest 객체를 사용하여 username과 boardId를 서비스 메서드에 전달합니다.
            Favorite favorite = mypageService.addFavorite(favoriteRequest.getUsername(), favoriteRequest.getBoardId());
            return ResponseEntity.ok().body(favorite);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to add favorite: " + e.getMessage());
        }
    }

    // 결제 요청이 온 게시글 목록 조회
    @GetMapping("/{userId}/payment-requests")
    public ResponseEntity<?> getPaymentRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(mypageService.getPaymentRequests(userId));
    }
    
    
    
    //FavoriteRequest DTO를 사용함으로써 클라이언트로부터 필요한 데이터(username, boardId)를
    //	구조화된 형태로 효율적으로 전달받고 처리하는 것을 목적
    private static class FavoriteRequest {
        private String username;
        private Integer boardId;

        // Getter와 Setter
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getBoardId() {
            return boardId;
        }

        public void setBoardId(Integer boardId) {
            this.boardId = boardId;
        }
    }
}
