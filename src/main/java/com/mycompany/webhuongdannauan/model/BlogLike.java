package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "blog_likes",
    // Đặt UniqueConstraint vào bên trong @Table
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"blog_post_id", "user_id"})
    }
)
public class BlogLike extends BaseEntity {

    // 1. N-1 với BlogPost
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    // 2. N-1 với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors và Getters/Setters...
    public BlogLike() {
        super();
    }
    
    public BlogPost getBlogPost() { return blogPost; }
    public void setBlogPost(BlogPost blogPost) { this.blogPost = blogPost; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}