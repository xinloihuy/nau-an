package com.mycompany.webhuongdannauan.model;


import jakarta.persistence.*;

@Entity
@Table(name = "follows")
// @UniqueConstraint({@UniqueConstraint(columnNames = {"follower_id", "followed_id"})})
public class Follow extends BaseEntity {
    
    // N-1 với User (Người theo dõi - Follower)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // N-1 với User (Người được theo dõi - Followed)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    // Constructors
    public Follow() { super(); }
    
    // Getters and Setters
    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }
    public User getFollowed() { return followed; }
    public void setFollowed(User followed) { this.followed = followed; }
}