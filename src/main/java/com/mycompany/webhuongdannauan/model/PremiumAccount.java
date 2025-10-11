package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "premium_accounts")
public class PremiumAccount extends BaseEntity {
    
    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // N-1 với User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Constructors
    public PremiumAccount() { super(); }

    // Getters and Setters
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}