package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "blog_posts")
public class BlogPost extends BaseEntity {
    
    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    // N-1 với User (Tác giả)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // 1-N với BlogComment (Composition)
    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BlogComment> comments;

    // 1-N với BlogLike (Composition)
    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BlogLike> likes;

    // ✅ Ngày tạo bài viết
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    // Constructors
    public BlogPost() { super(); }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Set<BlogComment> getComments() { return comments; }
    public void setComments(Set<BlogComment> comments) { this.comments = comments; }

    public Set<BlogLike> getLikes() { return likes; }
    public void setLikes(Set<BlogLike> likes) { this.likes = likes; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
