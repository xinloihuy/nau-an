package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cooking_tips")
public class CookingTip extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    // Constructors
    public CookingTip() { super(); }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}