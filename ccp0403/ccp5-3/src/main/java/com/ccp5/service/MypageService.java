package com.ccp5.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccp5.dto.Board;
import com.ccp5.dto.Favorite;
import com.ccp5.dto.MypageDTO;
import com.ccp5.dto.PaymentRequest;
import com.ccp5.dto.User;
import com.ccp5.repository.BoardRepository;
import com.ccp5.repository.FavoriteRepository;
import com.ccp5.repository.PaymentRequestRepository;
import com.ccp5.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MypageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    public MypageDTO getMypageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        List<Board> myBoards = boardRepository.findByWriter(user);
        // Favorite 및 PaymentRequest 정보도 비슷한 방식으로 조회
        
        // MypageDTO 생성 및 반환
        MypageDTO mypageDTO = new MypageDTO();
        mypageDTO.setUser(user);
        mypageDTO.setMyBoards(myBoards);
        // Set favorites and payment requests similarly
        
        return mypageDTO;
    }
    // 사용자 정보 조회 (예시)
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

  
    public List<Board> getUserPostsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) return Collections.emptyList(); // 사용자가 없으면 빈 리스트 반환
        return boardRepository.findByWriter(user);
    }

    // 사용자가 찜한 게시글 목록 조회
    public List<Favorite> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return favoriteRepository.findByUser(user);
    }

    // 결제 요청이 온 게시글 목록 조회
    public List<PaymentRequest> getPaymentRequests(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return paymentRequestRepository.findByUser(user);
    }
    
}
