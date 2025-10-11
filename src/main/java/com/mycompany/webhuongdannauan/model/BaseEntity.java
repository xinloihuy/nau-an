package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Constructors
    public BaseEntity() {
        this.createdAt = new Date(); // Khởi tạo thời gian tạo
        this.updatedAt = new Date();
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    // Không có setter cho createdAt, chỉ update qua logic
    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Phương thức tự động cập nhật updatedAt trước khi lưu
    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }
}