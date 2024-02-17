package com.vrx.electronic.store.dto;

import com.vrx.electronic.store.entity.CartItem;
import com.vrx.electronic.store.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {
    private String cartId;
    private Date createdOn;
    private UserDto user;
    private List<CartItemDto> cartItems = new ArrayList<>();
    private double totalPrice;
}
