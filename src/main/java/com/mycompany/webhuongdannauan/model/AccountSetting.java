/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "account_settings")
public class AccountSetting extends BaseEntity {

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    // Mối quan hệ N-1 với User (Composition: AccountSetting thuộc về User)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    // Constructors
    public AccountSetting() { super(); }

    // Getters and Setters
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean deleted) { isDeleted = deleted; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}