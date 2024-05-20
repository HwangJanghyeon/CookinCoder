package com.ccp5.service;

import com.ccp5.dto.Category;
import com.ccp5.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

   

	public Category findCategoryById(Long categoryId) {
		 Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
	        return optionalCategory.orElse(null); 
	}
}
