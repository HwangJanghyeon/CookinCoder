package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ccp5.dto.Board;
import com.ccp5.dto.User;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
	@Query(value = "select SUM(ROUND(b.unit * (c.cost / c.unit), -1)) from board a, ingredients_board b, data c where a.title=b.title and b.name=c.name and a.num =:num", nativeQuery = true)
	Integer calculateTotalPriceByNum(@Param("num") int num);
	
	// 해당 재료의 총 가격을 계산하는 메소드 추가
	@Query(value = "SELECT SUM(ROUND(b.unit * (c.cost / c.unit), -1)) FROM board a ,ingredients_board b, data c where a.title = b.title and b.name = c.name and a.num=:num and c.name = :ingredientName", nativeQuery = true)
	Integer calculateTotalPriceByIngredientName(@Param("ingredientName") String ingredientName, @Param("num") int num);

	@Query(value = "SELECT SUM(ROUND(b.unit * (c.cost / c.unit), -1)) + SUM(CASE WHEN c.name = :ingredientName THEN ROUND(b.unit * (c.cost / c.unit), -1) ELSE 0 END) FROM board a, ingredients_board b, data c WHERE a.title = b.title AND b.name = c.name AND a.num = :num", nativeQuery = true)
	Integer addPriceByIngredientName(@Param("num") int num, @Param("ingredientName") String ingredientName);

	@Query(value = "SELECT SUM(ROUND(b.unit * (c.cost / c.unit), -1)) - SUM(CASE WHEN c.name = :ingredientName THEN ROUND(b.unit * (c.cost / c.unit), -1) ELSE 0 END) FROM board a, ingredients_board b, data c WHERE a.title = b.title AND b.name = c.name AND a.num = :num", nativeQuery = true)
	Integer subtractPriceByIngredientName(@Param("num") int num, @Param("ingredientName") String ingredientName);


    List<Board> findByTitleContaining(String title);
    
    List<Board> findByCategoryId(Long categoryId);
    
    List<Board> findByWriter(User writer);
    
    Board findByNum(int boardNum);
}