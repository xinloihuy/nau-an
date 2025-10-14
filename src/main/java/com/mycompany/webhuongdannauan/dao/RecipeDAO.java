package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Recipe;
import java.util.List;

public interface RecipeDAO extends GenericDAO<Recipe, Long> {

    // Phương thức đặc thù cho Recipe
    List<Recipe> searchByKeyword(String keyword);
    List<Recipe> findFeaturedRecipes(int limit);
    List<Recipe> findByCategory(Long categoryId);
    Recipe findByIdWithCategories(Long recipeId);
    List<Recipe> findVipRecipes();
    List<Recipe> findRelatedRecipes(Recipe recipe, int limit);
    void updateViewCount(Long recipeId);
    long countAllRecipes();
    List<Recipe> filterRecipes(String keyword, Long categoryId, Integer maxTime, Boolean hasVideo, Boolean isVip); // <--- THÊM isVip

}