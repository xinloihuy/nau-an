package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite extends BaseEntity {
    
    // N-1 với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // N-1 với Recipe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    // Constructors
    public Favorite() { super(); }
    
    // Getters and Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}