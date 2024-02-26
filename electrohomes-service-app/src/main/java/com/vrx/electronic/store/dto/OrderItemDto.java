package com.vrx.electronic.store.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private double totalPrice;
    private ProductDto product;
}
