package com.ccp5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.Board;
import com.ccp5.dto.Category;
import com.ccp5.repository.BoardRepository;
import com.ccp5.repository.CategoryRepository;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final	BoardRepository boardRepository;
    
    @Autowired
    public CategoryController(CategoryRepository categoryRepository, BoardRepository boardRepository) {
        this.categoryRepository = categoryRepository;
        this.boardRepository = boardRepository;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // 특정 카테고리에 속하는 게시물들을 가져오는 메서드
    @GetMapping("/category/{categoryId}/boards")
    public ResponseEntity<List<Board>> getBoardsByCategory(@PathVariable Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.notFound().build(); // 카테고리가 존재하지 않으면 404 반환
        }

        List<Board> boards = boardRepository.findByCategoryId(categoryId); // 카테고리 ID에 해당하는 게시물 조회
        return ResponseEntity.ok(boards); // 조회된 게시물 목록 반환
    }
}
