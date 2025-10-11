package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private String status; // e.g., "COMPLETED", "PENDING", "FAILED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private PremiumPackage premiumPackage;
    
    @Column(name = "momo_order_id", unique = true)
    private String orderId; // Lưu Order ID của MoMo (dùng để tìm kiếm)
    
    @Column(name = "momo_trans_id", unique = true)
    private String transId; // Lưu Transaction ID thực tế từ MoMo khi thành công
    // END: CÁC TRƯỜNG BỔ SUNG CHO MOMO

    public Transaction() {
        super();
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PremiumPackage getPremiumPackage() {
        return premiumPackage;
    }

    public void setPremiumPackage(PremiumPackage premiumPackage) {
        this.premiumPackage = premiumPackage;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransId() {
        return transId;
    }
    public void setTransId(String transId) {
        this.transId = transId;
    }
}
