package com.ccp5.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ccp5.dto.User;
import com.ccp5.dto.User2;
import com.ccp5.repository.UserRepository2;
import com.ccp5.service.UserService2;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class); // 로거 선언
    @Autowired
    private  UserService2 userService2;
    @Autowired
    private UserRepository2 userRepository2;
    @Autowired
    private PasswordEncoder passwordEncoder;

  
    @GetMapping("/member/login") // 로그인 페이지 경로 수정
    public String login() {
        logger.info("Accessing login page"); // 로그 출력
        return "member/login";
    }

    @PostMapping("/loginPro")
    public String loginProcess(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        logger.info("Attempting login for username: {}", username); // 로그 출력
        User2 user = userRepository2.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 세션에 사용자 이름을 저장합니다.
            session.setAttribute("username", username);

            // 로그인 성공 후 리다이렉트할 페이지를 지정합니다.
            return "redirect:/index"; // 홈 페이지로 리다이렉트
        } else {
            // 로그인 실패 시 실패 이유를 모델에 담아 로그인 페이지로 이동합니다.
            model.addAttribute("error", "id or password error");
            logger.warn("Login failed for username: {}", username); // 로그 출력
            return "member/login"; // 로그인 페이지로 이동
        }
    }


    @GetMapping("/member/join")
    public String joinForm() {
        logger.info("Accessing join form page"); // 로그 출력
        return "member/join";
    }

    @PostMapping("/member/join")
    public String join(User2 user) {
        logger.info("Attempting to join with username: {}", user.getUsername()); // 로그 출력
        if (userRepository2.findByUsername(user.getUsername()) != null) {
            logger.warn("Username {} already exists", user.getUsername()); // 로그 출력
            return "fail";
        }
        // 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService2.join(user);
        logger.info("User {} successfully joined", user.getUsername()); // 로그 출력
        return "redirect:/member/login"; // 회원가입 후 로그인 페이지로 이동
    }
}
