package com.ccp5.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ccp5.dto.BoardDTO;
import com.ccp5.dto.IngrBoard;
import com.ccp5.repository.BoardRepository;
import com.ccp5.service.BoardService;
import com.ccp5.service.IngrListService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private IngrListService ilService;
    @Autowired
    private BoardRepository boardRepository;
    
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<BoardDTO> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "index";
    }

    @GetMapping("/view/{num}")
    public String view(@PathVariable("num") int num, Model model) {
        BoardDTO board = boardService.getBoardByNum(num);
        List<IngrBoard> ingrBoards = ilService.findByTitle(board.getTitle());
        Integer totalPrice = boardRepository.calculateTotalPriceByNum(num);
        model.addAttribute("board", board);
        model.addAttribute("ingrBoards", ingrBoards);
        model.addAttribute("total", totalPrice); // "total" 키를 사용하여 총 가격을 전달
        return "view";
    }
    @GetMapping("/ingredient/{num}")
    public String ingredient(@PathVariable("num") int num, Model model) {
        BoardDTO board = boardService.getBoardByNum(num);
        List<IngrBoard> ingrBoards = ilService.findByTitle(board.getTitle());
        Integer totalPrice = boardRepository.calculateTotalPriceByNum(num);
        model.addAttribute("board", board);
        model.addAttribute("ingrBoards", ingrBoards);
        model.addAttribute("total", totalPrice); // "total" 키를 사용하여 총 가격을 전달
        return "ingredient";
    }
    
    // 레시피 등록
    @GetMapping("/insert")
    public String inerst(Model model) {
    	model.addAttribute("categories", ilService.category());
    	return "insert";
    }
    @PostMapping("/insert")
    public String insertSubmit(@ModelAttribute BoardDTO boardDTO, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = boardService.uploadAndResizeImage(file); // 이미지 업로드 및 경로 반환
            boardDTO.setImageUrl(imageUrl); // 이미지 경로 설정
            boardService.insertBoard(boardDTO); // 게시글 등록
        } catch (IOException e) {
            // 이미지 업로드 중 오류 발생 시 예외 처리
            e.printStackTrace();
            // 에러 페이지 또는 다른 처리 방법 추가
        }
        return "redirect:/index";
    }
    // 레시피 수정폼
    @GetMapping("/update/{num}")
    public String update(@PathVariable int num, Model model) {
        BoardDTO board = boardService.getBoardByNum(num);
        List<IngrBoard> ingrBoards = ilService.findByTitle(board.getTitle());
        model.addAttribute("categories", ilService.category());
        model.addAttribute("iboard", ingrBoards);
        model.addAttribute("board", board);
        return "update";
    }
    
    @PostMapping("/updatePrice/{boardNum}")
    @ResponseBody
    public Integer updatePrice(@RequestBody Map<String, Object> requestBody, @PathVariable int boardNum) {
        // 요청으로부터 재료 이름과 버튼 텍스트를 가져옴
        String ingredientName = (String) requestBody.get("ingredientName");
        boolean isOwned = (boolean) requestBody.get("isOwned");
        System.out.println("ingredientName : "+ingredientName);
        System.out.println("isOwned : "+isOwned);
        
        // 버튼 텍스트가 '보유'인 경우에 해당하는 재료의 이름과 단위를 가져와 리포지토리의 쿼리에 전달하여 총 가격 계산
        if (!isOwned) {
            // 해당 재료의 총 가격을 계산하는 쿼리 실행
            Integer price = boardRepository.calculateTotalPriceByIngredientName(ingredientName, boardNum);
            System.out.println("totalprice : " + price);
            return price;
        }
        Integer price = boardRepository.calculateTotalPriceByIngredientName(ingredientName, boardNum) * -1;
        System.out.println(price);
        return price; // 보유 상태가 아닌 경우는 아직 처리하지 않음
    }
}