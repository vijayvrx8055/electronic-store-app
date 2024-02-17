package com.vrx.electronic.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String productId;
    @Column(name = "product_title")
    private String title;
    @Column(length = 10000)
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean isLive;
    private boolean inStock;
    private String productImageName;

}
