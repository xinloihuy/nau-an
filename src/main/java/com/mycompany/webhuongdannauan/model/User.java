package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;
    private Integer age;

    // Mối quan hệ N-N với Role qua bảng user_roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Mối quan hệ 1-1 với AccountSetting (Composition)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AccountSetting accountSetting;

    // Mối quan hệ 1-1 với PremiumAccount (Association - Trạng thái hiện tại)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private PremiumAccount premiumAccount;
    
    // Mối quan hệ 1-N với Recipe (Tác giả)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Recipe> recipes;

    // Constructors (BaseEntity có default constructor)
    public User() {
        super();
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public AccountSetting getAccountSetting() { return accountSetting; }
    public void setAccountSetting(AccountSetting accountSetting) { this.accountSetting = accountSetting; }
    public PremiumAccount getPremiumAccount() { return premiumAccount; }
    public void setPremiumAccount(PremiumAccount premiumAccount) { this.premiumAccount = premiumAccount; }
    public Set<Recipe> getRecipes() { return recipes; }
    public void setRecipes(Set<Recipe> recipes) { this.recipes = recipes; }
    
    // Phương thức kiểm tra quyền (Ví dụ)
    public boolean hasRole(String roleName) {
        return this.roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }
}