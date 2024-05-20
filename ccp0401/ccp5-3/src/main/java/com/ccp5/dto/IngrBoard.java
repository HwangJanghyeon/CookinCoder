package com.ccp5.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredients_board")
@Data
@NoArgsConstructor
public class IngrBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="inum")
    private int id;
    private String title;
    private String category;
    private String name;
    private int unit;
    
    private String imageUrl;
   
    public int getId() {
		return id;
	}


	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getUnit() {
		return unit;
	}


	public void setUnit(int unit) {
		this.unit = unit;
	}


	// 생성자
    public IngrBoard(String title, String category, String name, int unit) {
        this.title = title;
        this.category = category;
        this.name = name;
        this.unit = unit;
    }
}

