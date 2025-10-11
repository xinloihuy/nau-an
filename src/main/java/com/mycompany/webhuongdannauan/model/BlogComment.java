package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "blog_comments")
public class BlogComment extends BaseEntity {

    @Lob
    private String content;

    // 1. N-1 với BlogPost (Composition - Comment không tồn tại nếu BlogPost bị xóa)
    // Mối quan hệ này phải được xử lý cascade ở phía BlogPost (1-N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    // 2. N-1 với User (Association - Người bình luận)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public BlogComment() {
        super();
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}