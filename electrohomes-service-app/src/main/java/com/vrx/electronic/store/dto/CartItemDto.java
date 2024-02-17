package com.vrx.electronic.store.dto;

import com.vrx.electronic.store.entity.Cart;
import com.vrx.electronic.store.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemDto {
    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private double totalPrice;
}
