/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webhuongdannauan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "premium_packages")
public class PremiumPackage extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Monthly", "Yearly"

    @Column(nullable = false)
    private long price;

    @Column(name = "duration_days", nullable = false)
    private Integer durationInDays;

    // Constructors
    public PremiumPackage() { super(); }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public Integer getDurationInDays() { return durationInDays; }
    public void setDurationInDays(Integer durationInDays) { this.durationInDays = durationInDays; }
}