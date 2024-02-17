package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.dto.CartDto;
import com.vrx.electronic.store.dto.request.AddItemToCartRequest;
import com.vrx.electronic.store.entity.Cart;
import com.vrx.electronic.store.entity.CartItem;
import com.vrx.electronic.store.entity.Product;
import com.vrx.electronic.store.entity.User;
import com.vrx.electronic.store.exception.BadApiRequest;
import com.vrx.electronic.store.exception.ResourceNotFoundException;
import com.vrx.electronic.store.repository.CartItemRepository;
import com.vrx.electronic.store.repository.CartRepository;
import com.vrx.electronic.store.repository.ProductRepository;
import com.vrx.electronic.store.repository.UserRepository;
import com.vrx.electronic.store.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        if (request.getQuantity() <= 0) {
            throw new BadApiRequest("Quantity should be greater than 0!");
        }

        //fetch the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found!"));
        //fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        //In order to add something to cart, first we need the cart
        //fetch cart if already present, otherwise create new one
        Cart cart = cartRepository.findByUser(user)
                .orElse(Cart.builder()
                        .cartId(UUID.randomUUID().toString())
                        .createdOn(new Date())
                        .build());
        //fetch all items of cart
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> cartItems = cart.getCartItems();

        if (cart.getCartItems() != null) {
            List<CartItem> collectedItems = cartItems.stream().map(item -> {
                if (item.getProduct().getProductId().equals(request.getProductId())) {
                    item.setQuantity(request.getQuantity());
                    item.setTotalPrice(request.getQuantity() * product.getDiscountedPrice());
                    updated.set(true);
                }
                return item;
            }).collect(Collectors.toList());
            cart.setCartItems(collectedItems);
        } else {
            cart.setCartItems(new ArrayList<>());
        }
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .quantity(request.getQuantity())
                    .totalPrice(request.getQuantity() * product.getDiscountedPrice())
                    .build();
            cart.getCartItems().add(cartItem);
        }
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);
        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found!"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found!"));
        cart.getCartItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found!"));
        return modelMapper.map(cart, CartDto.class);

    }

}
