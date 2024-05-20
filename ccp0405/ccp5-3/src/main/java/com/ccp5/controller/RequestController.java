package com.ccp5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccp5.dto.RequestBoard;
import com.ccp5.service.RequestBoardSevice;



@Controller
@RequestMapping("/request/*")
public class RequestController {
	@Autowired
	private RequestBoardSevice rbService;
	
	// 요청 게시글 작성폼
	@GetMapping("/request_insert")
	public String r_insert() {
		return "/request/request_insert";
	}
	// 요청 게시글 추가
	@PostMapping("/request_insert")
	public String r_insert(RequestBoard rboard) {
		rbService.insert(rboard);
		return "redirect:/request/request_list";
	}
	
	// 요청 게시글 수정폼
	@GetMapping("/request_update/{num}")
	public String update(@PathVariable("num") int num, Model model) {
		model.addAttribute("rBoard", rbService.findByNum(num));
		return "/request/request_update";
	}
	// 요청 게시글 수정
    @RequestMapping(value = "/request_update/{num}", method=RequestMethod.POST)
    public String update(@PathVariable("num") int num, RequestBoard rBoard) {
        rBoard.setNum(num); // URL에 있는 번호로 업데이트할 게시글 번호 설정
        rbService.update(rBoard);
        return "redirect:/request/request_list";
    }
	
	// 요청 게시글 불러오기
	@GetMapping("/request_list")
	public String r_list(Model model) {
		model.addAttribute("boards",rbService.list());
		return "/request/request_list";
	}
	
	
	// 요청 게시글 자세히 보기
	@GetMapping("/request_view/{num}")
	public String r_view(@PathVariable("num") int num, Model model) {
		model.addAttribute("rBoard", rbService.findByNum(num));
		return "request/request_view";
	}
	
	 // 요청 게시글 삭제
    @RequestMapping(value = "/delete/{num}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable int num) {
        rbService.delete(num);
        return ResponseEntity.ok("삭제 성공");
    }
}
