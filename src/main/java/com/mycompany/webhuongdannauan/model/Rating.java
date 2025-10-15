package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating extends BaseEntity {
    
    // Điểm đánh giá (1-5 sao)
    @Column(nullable = false)
    private Integer score; // từ 1 đến 5
    
    // N-1 với User (Người đánh giá)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // N-1 với Recipe (Món ăn được đánh giá)
    @ManyToOne(fetch = FetchType.LAZY) // Nên đổi thành LAZY để tối ưu hiệu năng
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
    
    // Constructors
    public Rating() { 
        super(); 
    }
    
    // Getters and Setters
    public Integer getScore() { 
        return score; 
    }
    
    public void setScore(Integer score) { 
        // Validate: chỉ cho phép 1-5 sao
        if (score < 1) score = 1;
        if (score > 5) score = 5;
        this.score = score; 
    }
    
    public User getUser() { 
        return user; 
    }
    
    public void setUser(User user) { 
        this.user = user; 
    }
    
    public Recipe getRecipe() { 
        return recipe; 
    }
    
    public void setRecipe(Recipe recipe) { 
        this.recipe = recipe; 
    }
}