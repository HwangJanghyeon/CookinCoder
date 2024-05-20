package com.ccp5.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ccp5.dto.User;
import com.ccp5.repository.UserRepository;
import com.ccp5.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class UserController {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class); // 로거 선언
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private final ObjectMapper objectMapper;

	public UserController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@GetMapping("/member/login") // 로그인 페이지 경로 수정
	public String login() {
		logger.info("Accessing login page"); // 로그 출력
		return "member/login";
	}

	@PostMapping("/member/login")
	public String loginProcess(@RequestBody Map<String, String> credentials, HttpSession session, Model model) {
		String username = credentials.get("username");
		String password = credentials.get("password");

		logger.info("Attempting login for username: {}", username); // 로그 출력
		User user = userRepository.findByUsername(username);
		if (user != null && password.equals(user.getPassword())) { // 비밀번호 비교

			session.setAttribute("username", username);

			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "Login successful");
			model.addAttribute("responseMap", responseMap);
			return "login-success"; // 로그인 성공 페이지로 이동
		} else {
			// 로그인 실패 시 실패 이유를 반환합니다.
			// 응답 데이터를 Map 형식으로 구성
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "Invalid username or password");
			model.addAttribute("responseMap", responseMap);
			return "login-failure"; // 로그인 실패 페이지로 이동
		}
	}

	@GetMapping("/member/join")
	public String joinForm() {
		logger.info("Accessing join form page"); // 로그 출력
		return "member/join";
	}

	@PostMapping("/member/join")
	public String join(@RequestBody User user, Model model) {
		logger.info("Attempting to join with username: {}", user.getUsername()); // 로그 출력
		if (userRepository.findByUsername(user.getUsername()) != null) {
			logger.warn("Username {} already exists", user.getUsername()); // 로그 출력
			model.addAttribute("message", "Username already exists");
			return "join-failure"; // 회원가입 실패 페이지로 이동
		}
		// 비밀번호를 암호화하여 저장
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userService.join(user);
		logger.info("User {} successfully joined", user.getUsername()); // 로그 출력

		// 응답 데이터를 Map 형식으로 구성
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("message", "User successfully joined");
		responseMap.put("username", user.getUsername());
		model.addAttribute("responseMap", responseMap);
		return "join-success"; // 회원가입 성공 페이지로 이동
	}
}
