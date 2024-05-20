package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ccp5.dto.BoardDTO;
import com.ccp5.dto.User;

@Repository
public interface BoardRepository extends JpaRepository<BoardDTO, Integer> {
	@Query(value = "select SUM(ROUND(b.unit * (c.cost / c.unit), -1)) from board a, ingredients_board b, data c where a.title=b.title and b.name=c.name and a.num =:num", nativeQuery = true)
	Integer calculateTotalPriceByNum(@Param("num") int num);
	
	// 해당 재료의 총 가격을 계산하는 메소드 추가
    @Query(value = "SELECT SUM(ROUND(b.unit * (c.cost / c.unit), -1)) FROM board a ,ingredients_board b, data c where a.title = b.title and b.name = c.name and a.num=:num and c.name = :ingredientName",
            nativeQuery = true)
    Integer calculateTotalPriceByIngredientName(@Param("ingredientName") String ingredientName, @Param("num") int num);

    List<BoardDTO> findByTitleContaining(String title);
    
    List<BoardDTO> findByCategoryId(Long categoryId);
    
    List<BoardDTO> findByWriter(User writer);
}
