package com.ccp5.dto;



import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IngrList {
	private String category;
	private String name;
	private int unit;
	private long cost;
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
	public long getCost() {
		return cost;
	}
	public void setCost(long cost) {
		this.cost = cost;
	}
	
}
