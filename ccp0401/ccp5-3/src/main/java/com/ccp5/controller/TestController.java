package com.ccp5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ccp5.dto.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/list")
	public String getList(Model model) {
	model.addAttribute("num",10);
	return "/test/teacher";
	
	}
	
	@GetMapping("/one")
	public User getOne() {
		
		User user = new User();
		return user;
		
	}
}
