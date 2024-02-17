package com.vrx.electronic.store.dto;

import com.vrx.electronic.store.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private String productId;
    private String title;
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean isLive; // live
    private boolean inStock;
    private String productImageName;
    private CategoryDto category;
}
