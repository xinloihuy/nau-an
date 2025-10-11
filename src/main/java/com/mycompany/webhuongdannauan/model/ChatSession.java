package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "chat_sessions")
public class ChatSession extends BaseEntity {

    @Column(name = "last_message_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMessageAt;

    @Column(nullable = false)
    private String status; // e.g., "OPEN", "CLOSED", "PENDING"

    // N-1 với User (User cần hỗ trợ)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 1-N với ChatMessage (Composition: Tin nhắn thuộc về phiên chat)
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChatMessage> messages;

    // Constructors
    public ChatSession() { super(); }

    // Getters and Setters
    public Date getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Date lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Set<ChatMessage> getMessages() { return messages; }
    public void setMessages(Set<ChatMessage> messages) { this.messages = messages; }
}