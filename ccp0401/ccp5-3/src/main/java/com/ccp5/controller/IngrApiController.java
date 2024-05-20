package com.ccp5.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ccp5.dto.BoardDTO;
import com.ccp5.dto.Category;
import com.ccp5.dto.DataDTO;
import com.ccp5.dto.IngrBoard;
import com.ccp5.dto.User;
import com.ccp5.repository.BoardRepository;
import com.ccp5.service.BoardService;
import com.ccp5.service.CategoryService;
import com.ccp5.service.IngrListService;
import com.ccp5.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
@RequiredArgsConstructor
public class IngrApiController {
    @Autowired
    private IngrListService ilService;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private BoardService boardService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping(path = "/api/get_names", produces = MediaType.APPLICATION_JSON_VALUE)// 서버가 미디어타입을 text/plain 으로 받아서 수정
    public ResponseEntity<String> getNamesByCategory(@RequestParam(value = "categoryId", required = false) String categoryId) {
        List<DataDTO> names;
        if (categoryId == null || categoryId.isEmpty() || "all".equals(categoryId)) {
            // 'categoryId'가 제공되지 않았거나 'all'인 경우, 모든 데이터를 조회
            names = ilService.findAllNames();
        } else {
            // 'categoryId'에 해당하는 데이터만 조회
            names = ilService.findNames(categoryId);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonNames = objectMapper.writeValueAsString(names);
            return ResponseEntity.ok(jsonNames);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing JSON");
        }
    }
    @PostMapping("/api/submit_all_forms")
    public ResponseEntity<String> submitAllForms(@RequestBody List<IngrBoard> forms) {
        log.info("Received request to submit all forms");

        // 폼 데이터 처리
        for (IngrBoard form : forms) {
            // 각 폼에 대한 처리 로직 수행
            // ingredientService.save(form); // 예시: 폼 데이터를 서비스로 전달하여 저장
            ilService.insertIngr(form);
        }
        
        // JSON 형식으로 응답 반환
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "All forms submitted successfully");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(responseMap);
            log.info("All forms submitted successfully");
        } catch (JsonProcessingException e) {
            log.error("Error converting response to JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting response to JSON");
        }
        return ResponseEntity.ok(jsonResponse);
    }
    @PostMapping("/api/submit_recipe")
    public ResponseEntity<String> submitRecipe(@RequestParam("title") String title,
            @RequestParam("content") String content, @RequestParam("categoryId") Long categoryId,
            @RequestParam("image") MultipartFile file, @RequestParam("username") String username) {
        log.info("Received request to submit recipe: {}", title);

        // 작성자의 이름으로 사용자 정보 가져오기
        User writer = userService.findByUsername(username);
        if (writer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with username: " + username);
        }

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle(title);
        boardDTO.setContent(content);
        boardDTO.setWriter(writer); // 작성자 정보 설정

        // 여기에서 카테고리 ID를 처리합니다.
        Category category = categoryService.findCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found with ID: " + categoryId);
        }
        boardDTO.setCategory(category);

        try {
            if (!file.isEmpty()) {
                String imageUrl = boardService.uploadAndResizeImage(file);
                boardDTO.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            log.error("Error occurred while uploading image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading image");
        }

        try {
            boardService.insertBoard(boardDTO);
            log.info("Recipe submitted successfully: {}", title);
            return ResponseEntity.ok("Recipe submitted successfully");
        } catch (Exception e) {
            log.error("Error occurred while submitting recipe", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while submitting recipe");
        }
    }



    // 레시피 및 재료 수정
    @PostMapping("/api/submit_all_forms-update")
    public ResponseEntity<Void> submitAllFormsUpdate(@RequestBody List<IngrBoard> ingredientForms) {
        boardService.updateIngredientForms(ingredientForms);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/submit_recipe_update")
    public ResponseEntity<Void> submitRecipeUpdate(@RequestBody BoardDTO recipeForm) {
        boardService.updateRecipeForm(recipeForm);
        return ResponseEntity.ok().build();
    }

    // 레시피 및 재료 삭제
    @DeleteMapping("/api/delete/{num}")
    public ResponseEntity<Integer> deleteRecipe(@PathVariable int num, @RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        boardService.deleteRecipe(num, title);
        return ResponseEntity.ok(num);
    }
}
