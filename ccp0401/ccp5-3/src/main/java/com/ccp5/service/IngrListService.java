package com.ccp5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccp5.dto.DataDTO;
import com.ccp5.dto.IngrBoard;
import com.ccp5.repository.DataRepository;
import com.ccp5.repository.IngrListRepository;

@Service
public class IngrListService {
	@Autowired
	private IngrListRepository ilRepo;
	@Autowired
	private DataRepository dRepo;

	public List<String> category() {
		return dRepo.findDistinctCategories();
	}
	
	public List<DataDTO> findNames(String categoryId) {
		return dRepo.findByCategory(categoryId);
	}
	public List<DataDTO> findAllNames() {
		
		return dRepo.findAll();
	}

	// 입력 페이지에서 받은 재료들을 DB로 저장
	public void insertIngr(IngrBoard iboard) {
		ilRepo.save(iboard);
	}
	// title과 일치하는 재료 테이블 불러오기
	public List<IngrBoard> findByTitle(String title) {
        return ilRepo.findByTitle(title);
    }
	// 등록된 재료 목록 수정
	public void updateIngr(IngrBoard iboard) {
		ilRepo.save(iboard);
	}

	
	
}
