package com.ccp5.dto;

public class UpdatePriceRequest {
    private String ingredientName;
    private boolean isOwned;

    // 기본 생성자
    public UpdatePriceRequest() {
    }

    // getter와 setter
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public void setOwned(boolean isOwned) {
        this.isOwned = isOwned;
    }
}
