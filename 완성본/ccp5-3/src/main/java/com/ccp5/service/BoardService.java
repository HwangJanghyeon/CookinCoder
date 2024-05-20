package com.ccp5.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccp5.dto.Board;
import com.ccp5.dto.IngrBoard;
import com.ccp5.dto.User;
import com.ccp5.repository.BoardRepository;
import com.ccp5.repository.IngrListRepository;
import com.ccp5.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service

public class BoardService {
	@Autowired
	public  BoardRepository boardRepo;
	@Autowired
	public IngrListRepository ilRepo;
	@Autowired
	private UserRepository userRepository;
	
	 @Value("${upload.dir}")
	 private String uploadDir;

	// 메인
	public List<Board> getAllBoards() {
		// JPA Repository 사용
		return boardRepo.findAll();
	}

	// 뷰
	public Board getBoardByNum(int num) {
		// JPA Repository 사용
		return boardRepo.findById(num).orElse(null);
	}
	public Board getIngredientByNum(int num) {
		// JPA Repository 사용
		return boardRepo.findById(num).orElse(null);
	}
	
	// 레시피 작성
	public void insertBoard(Board board) {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsername(username);
        if(user != null) {
           board.setWriter(user);
        }
       
		boardRepo.save(board);
	}

	// 레시피 수정
	@Transactional
	public void updateIngredientForms(List<IngrBoard> ingredientForms) {
		for (IngrBoard ingrBoard : ingredientForms) {
			ilRepo.deleteByTitle(ingrBoard.getTitle());
		}
		ilRepo.deleteByNull();
		
		List<IngrBoard> filteredIngredientForms = ingredientForms.stream().filter(ingrBoard -> ingrBoard.getUnit() != 0)
				.collect(Collectors.toList());
		ilRepo.saveAll(filteredIngredientForms);
	}
	@Transactional
	public void updateRecipeForm(Board recipeForm) {
		boardRepo.save(recipeForm);
	}
	// 레시피 삭제
		@Transactional
		public void deleteRecipe(int num, String title) {
			boardRepo.deleteById(num);
			ilRepo.deleteByTitle(title);
		}
	@Transactional
	public void deleteIngredientForms(List<IngrBoard> ingredientForms) {
		for (IngrBoard ingrBoard : ingredientForms) {
			ilRepo.deleteByTitle(ingrBoard.getTitle());
		}
	}
	 public String uploadAndResizeImage(MultipartFile file) throws IOException {
	        // 기존 이미지 파일의 개수 확인
	        int imageCount = Files.list(Paths.get(uploadDir))
	                .filter(Files::isRegularFile)
	                .map(Path::getFileName)
	                .map(Path::toString)
	                .filter(fileName -> fileName.startsWith("pic"))
	                .mapToInt(fileName -> Integer.parseInt(fileName.substring(3, 5)))
	                .max()
	                .orElse(0);

	        // 새 이미지 파일 이름 설정
	        String fileName = String.format("pic%02d.jpg", imageCount + 1);

	        // 파일 경로 설정
	        Path filePath = Paths.get(uploadDir + fileName);

	        // 업로드된 파일을 임시로 저장
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        // 이미지 크기 조절
	        BufferedImage originalImage = ImageIO.read(filePath.toFile());
	        BufferedImage resizedImage = new BufferedImage(353, 326, BufferedImage.TYPE_INT_RGB);
	        resizedImage.createGraphics().drawImage(originalImage.getScaledInstance(353, 326, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
	        ImageIO.write(resizedImage, "jpg", filePath.toFile());

	        // 이미지 경로 반환
	        return "/images/" + fileName;
	    }

	  public List<Board> searchByTitle(String title) {
	 
	        return boardRepo.findByTitleContaining(title);
	    }
	
	
	}

