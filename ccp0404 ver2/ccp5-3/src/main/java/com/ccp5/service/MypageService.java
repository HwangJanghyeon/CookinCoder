package com.ccp5.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Board> getUserFavorites(Long userId) {
        // 사용자 ID를 기반으로 해당 사용자를 조회
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        // 사용자 객체를 이용하여 해당 사용자가 찜한 favorite 목록을 조회
        List<Favorite> favorites = favoriteRepository.findByUser(user);
        
        // 조회된 favorite 목록에서 게시글을 추출하여 리스트에 담음
        List<Board> favoriteBoards = favorites.stream()
                                              .map(Favorite::getBoard)
                                              .collect(Collectors.toList());
        
        return favoriteBoards;
    }

    // 찜하기 저장
    public Favorite addFavorite(String username, Integer boardId) {
        User user = userRepository.findByUsername(username);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBoard(board);

        return favoriteRepository.save(favorite);
    }
 // 결제 요청이 온 게시글 목록 조회
    public List<Board> getPaymentRequests(Long userId) {
        // 사용자 ID를 기반으로 해당 사용자를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 사용자 객체를 이용하여 해당 사용자의 결제 요청 목록을 조회
        List<PaymentRequest> paymentRequests = paymentRequestRepository.findByUser(user);

        // 조회된 결제 요청 목록에서 게시글을 추출하여 리스트에 담음
        List<Board> paymentBoards = paymentRequests.stream()
                .map(PaymentRequest::getBoard)
                .collect(Collectors.toList());

        return paymentBoards;
    }
    
}
