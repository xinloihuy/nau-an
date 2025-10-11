/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Lob
    private String content;

    @Column(nullable = false)
    private String type; // e.g., 'FOLLOW', 'ADMIN_MESSAGE', 'RECIPE_COMMENT'

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // N-1 với User (Người nhận thông báo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Notification() { super(); }

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean read) { isRead = read; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}