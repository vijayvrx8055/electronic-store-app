package com.vrx.electronic.store.controller;

import com.vrx.electronic.store.dto.CartDto;
import com.vrx.electronic.store.dto.request.AddItemToCartRequest;
import com.vrx.electronic.store.dto.response.ApiResponseMessage;
import com.vrx.electronic.store.entity.Cart;
import com.vrx.electronic.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@Controller
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    //add items to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request,
                                                 @PathVariable String userId) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    //remove item from card
    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int cartItemId) {
        cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Item removed successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //clear items
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Item removed successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //getCart

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getAllItemsFromCart(@PathVariable String userId) {
        CartDto cartByUserId = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(cartByUserId, HttpStatus.OK);
    }
}
