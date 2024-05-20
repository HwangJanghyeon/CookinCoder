package com.ccp5.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ccp5.dto.DataDTO;
import com.ccp5.dto.IngrBoard;
import com.ccp5.service.BoardService;
import com.ccp5.service.IngrListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IngrController {
    
    @Autowired
    private IngrListService ilService;
    
    @Autowired
    private  BoardService boardService;

    @GetMapping("/get_names")
    public String getNamesByCategory(@RequestParam("categoryId") String categoryId) {
        // categoryId를 이용하여 해당하는 카테고리에 속하는 이름 목록을 데이터베이스에서 조회하고 반환
        List<DataDTO> names = ilService.findNames(categoryId);
        // 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonNames = objectMapper.writeValueAsString(names);
            return jsonNames;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error occurred while processing JSON";
        }
    }

    // 레시피 및 재료 등록
    @PostMapping("/submit_all_forms")
    public ResponseEntity<String> submitAllForms(@RequestBody List<IngrBoard> forms) {
        // 폼 데이터 처리
        for (IngrBoard form : forms) {
            // 각 폼에 대한 처리 로직 수행
            // ingredientService.save(form); // 예시: 폼 데이터를 서비스로 전달하여 저장
            ilService.insertIngr(form);
        }
        return ResponseEntity.ok("All forms submitted successfully");
    }
    
    @PostMapping("/submit_recipe")
    public String submitRecipe(@RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("image") MultipartFile file) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle(title);
        boardDTO.setContent(content);

        if (!file.isEmpty()) {
            try {
                String imageUrl = boardService.uploadAndResizeImage(file);
                boardDTO.setImageUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        boardService.insertBoard(boardDTO);

        return "redirect:/index";
    }

    
    // 레시피 및 재료 수정
    @PostMapping("/submit_all_forms-update")
    public void submitAllFormsUpdate(@RequestBody List<IngrBoard> ingredientForms) {
        boardService.updateIngredientForms(ingredientForms);
    }
    
    @PutMapping("/submit_recipe_update")
    public void submitRecipeUpdate(@RequestBody BoardDTO recipeForm) {
        boardService.updateRecipeForm(recipeForm);
    }
    
    // 레시피 및 재료 삭제
    @DeleteMapping("/delete/{num}")
    public int deleteRecipe(@PathVariable int num, @RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        boardService.deleteRecipe(num, title);
        return num;
    }
}