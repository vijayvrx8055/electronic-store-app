package com.vrx.electronic.store.service;

import com.vrx.electronic.store.dto.CartDto;
import com.vrx.electronic.store.dto.request.AddItemToCartRequest;
import com.vrx.electronic.store.entity.CartItem;

public interface CartService {

    //add item to cart
    //case1: if cart is not available, then first create cart
    //case2: if cart is available, then add items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //clearCart
    void clearCart(String userId);

    //get cart details
    CartDto getCartByUserId(String userId);


}
