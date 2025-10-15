package com.mycompany.webhuongdannauan.dto;

import com.mycompany.webhuongdannauan.model.Recipe;

/**
 * Lớp này dùng để "gói" một công thức yêu thích cùng với thông tin đánh giá của nó
 * để tiện gửi sang trang JSP.
 */
public class FavoriteRecipeInfo {

    private Recipe recipe;
    private double avgRating;
    private long ratingCount;

    public FavoriteRecipeInfo(Recipe recipe, double avgRating, long ratingCount) {
        this.recipe = recipe;
        this.avgRating = avgRating;
        this.ratingCount = ratingCount;
    }

    // Getters and Setters
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(long ratingCount) {
        this.ratingCount = ratingCount;
    }
}