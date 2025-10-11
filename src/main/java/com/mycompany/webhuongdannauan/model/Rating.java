package com.mycompany.webhuongdannauan.model;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating extends BaseEntity {

    @Column(nullable = false)
    private Integer score; // 1-5

    // N-1 với User (Người đánh giá)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // N-1 với Recipe (Composition - Đánh giá thuộc về món ăn)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    // Constructors
    public Rating() { super(); }
    
    // Getters and Setters
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}