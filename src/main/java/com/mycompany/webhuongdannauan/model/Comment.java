package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Lob
    private String content;

    // N-1 với User (Tác giả bình luận)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // N-1 với Recipe (Bình luận thuộc về món ăn)
    @ManyToOne(fetch = FetchType.LAZY) // Nên đổi thành LAZY để tối ưu hiệu năng
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    // Constructors
    public Comment() { super(); }
    
    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}