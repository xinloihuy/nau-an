package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {

    @Lob
    private String content;

    @Column(name = "sender_role", nullable = false)
    private String senderRole; // e.g., "USER", "ADMIN"

    // N-1 với ChatSession
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    // Constructors
    public ChatMessage() { super(); }

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }
    public ChatSession getSession() { return session; }
    public void setSession(ChatSession session) { this.session = session; }
}