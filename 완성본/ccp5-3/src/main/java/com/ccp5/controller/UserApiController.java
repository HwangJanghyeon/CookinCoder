package com.ccp5.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.User;
import com.ccp5.repository.UserRepository;
import com.ccp5.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class UserApiController {

    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class); // 로거 선언
    @Autowired
    private  UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserApiController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @GetMapping("/api/login")
    public String login() {
        logger.info("Accessing login page"); // 로그 출력
        return "member/login";
    }
    
    @PostMapping("/api/login")
    public ResponseEntity<?> loginProcess(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        logger.info("Attempting login for username: {}", username);
        User user = userRepository.findByUsername(username);

        if (user != null && password.equals(user.getPassword())) { // 비밀번호 비교
            session.setAttribute("username", username);

            // 사용자 ID를 가져와서 response에 추가
            Long userId = user.getId();

            // 로그인 성공 시, 반환될 JSON 구조를 만듭니다.
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Login successful");
            responseMap.put("username", username);
            responseMap.put("id", userId); // 사용자 ID 추가
            // 실제 사용 시에는 JWT 토큰을 생성하고 반환합니다.
            String fakeToken = "generated-token-placeholder";
            responseMap.put("token", fakeToken);
            logger.info("Login successful for username: {}, userId: {}", username, userId);

            String jsonResponse = "";
            try {
                jsonResponse = objectMapper.writeValueAsString(responseMap);
                logger.info("Login successful for username: {}", username);
               
            } catch (JsonProcessingException e) {
                logger.error("Error converting response to JSON", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting response to JSON");
            }

            return ResponseEntity.ok(jsonResponse);
        } else {
            logger.warn("Login attempt failed for username: {}", username);

            // 로그인 실패 시, 반환될 JSON 구조를 만듭니다.
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Invalid username or password");

            String jsonResponse = "";
            try {
                jsonResponse = objectMapper.writeValueAsString(responseMap);
            } catch (JsonProcessingException e) {
                logger.error("Error converting response to JSON", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting response to JSON");
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }
    }




    @GetMapping("/api/join")
    public String joinForm() {
        logger.info("Accessing join form page"); // 로그 출력
        return "member/join";
    }

    @PostMapping("/api/join")
    public ResponseEntity<String> join(@RequestBody User user) {
        logger.info("Attempting to join with username: {}", user); // 로그 출력
        
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.warn("Username {} already exists", user.getUsername()); // 로그 출력
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists"); // 이미 존재하는 사용자
        }
   

        userService.join(user);
        logger.info("User {} successfully joined", user.getUsername()); // 로그 출력

        // 응답 데이터를 Map 형식으로 구성
        Map<String, Object> responseMap = new HashMap<>();
        logger.info("responseMap", responseMap);
        responseMap.put("message", "User successfully joined");
        responseMap.put("username", user.getUsername());
        
        // ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(responseMap);
            logger.info("jsonResponse",jsonResponse);
        } catch (JsonProcessingException e) {
            log.error("Error converting response to JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting response to JSON");
        }

        // ResponseEntity를 사용하여 JSON 형식의 응답 반환
        return ResponseEntity.ok(jsonResponse);
    }
}


