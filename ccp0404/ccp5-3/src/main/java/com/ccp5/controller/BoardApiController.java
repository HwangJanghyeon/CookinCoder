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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.Board;
import com.ccp5.dto.IngrBoard;
import com.ccp5.dto.UpdatePriceRequest;
import com.ccp5.repository.BoardRepository;
import com.ccp5.service.BoardService;
import com.ccp5.service.IngrListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequestMapping("/api/boards")
public class BoardApiController {
	
	@Autowired
	BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
	private IngrListService ilService;

    @GetMapping("/search")
    public ResponseEntity<List<Board>> searchBoards(@RequestParam String title) {
        // 검색 로직 구현
        List<Board> searchResults = boardService.searchByTitle(title);
        log.info("Search results: {}", searchResults);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllBoards() {
        List<Board> boards = boardService.getAllBoards();
        log.info("Retrieved all boards: {}", boards);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            if (boards != null && !boards.isEmpty()) {
                String jsonNames = objectMapper.writeValueAsString(boards);
                return ResponseEntity.ok(jsonNames);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (JsonProcessingException e) {
            log.error("Error occurred while processing JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing JSON");
        }
    }

    @GetMapping("/{num}")
    public ResponseEntity<Board> getBoardByNum(@PathVariable int num) {
        Board board = boardService.getBoardByNum(num);
        log.info("Retrieved board with number {}: {}", num, board);
        if (board != null) {
            return ResponseEntity.ok(board);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{num}/ingredients")
    public ResponseEntity<List<IngrBoard>> getIngredientsForBoard(@PathVariable int num) {
        Board board = boardService.getBoardByNum(num);
        log.info("Retrieved board with number {}: {}", num, board);
        List<IngrBoard> ingrBoards = ilService.findByTitle(board.getTitle());
        log.info("Ingredients for board {}: {}", num, ingrBoards);
        if (ingrBoards != null && !ingrBoards.isEmpty()) {
            return ResponseEntity.ok(ingrBoards);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{num}/totalPrice")
    public ResponseEntity<Integer> getTotalPrice(@PathVariable int num) {
        log.info("Calculating total price for board with number {}", num);
        Integer totalPrice = boardRepository.calculateTotalPriceByNum(num);
        log.info("Total price for board with number {}: {}", num, totalPrice);
        if (totalPrice != null) {
            return ResponseEntity.ok(totalPrice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{boardNum}/calculatePrice")
    @ResponseBody
    public Integer updatePrice(@RequestBody UpdatePriceRequest requestBody, @PathVariable int boardNum) {
        // 요청으로부터 재료 이름과 보유 여부를 가져옴
        String ingredientName = requestBody.getIngredientName();
        boolean isOwned = requestBody.isOwned();
        System.out.println("ingredientName : " + ingredientName);
        System.out.println("isOwned : " + isOwned);

        // 보유 여부에 따라 총 가격을 갱신하는 쿼리 실행
        Integer price;
        if (!isOwned) {
            // 해당 재료의 가격을 총 가격에서 빼는 쿼리 실행
            price = boardRepository.subtractPriceByIngredientName(boardNum,ingredientName);
        } else {
            // 해당 재료의 가격을 총 가격에 더하는 쿼리 실행
        	 price = boardRepository.addPriceByIngredientName(boardNum, ingredientName);

        }

        // 계산된 가격을 반환
        return price;
    }

}
