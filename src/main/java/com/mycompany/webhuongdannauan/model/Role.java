/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webhuongdannauan.model;


import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String name; // e.g., 'ADMIN', 'PREMIUM', 'USER'

    // Mối quan hệ N-N với User (không cần mappedBy nếu không muốn truy cập ngược từ Role)
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    // Constructors
    public Role() { super(); }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}