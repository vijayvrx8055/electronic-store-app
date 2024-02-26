package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.dto.OrderDto;
import com.vrx.electronic.store.dto.request.OrderRequest;
import com.vrx.electronic.store.dto.response.OrderResponse;
import com.vrx.electronic.store.dto.response.PageableResponse;
import com.vrx.electronic.store.entity.*;
import com.vrx.electronic.store.exception.BadApiRequest;
import com.vrx.electronic.store.exception.ResourceNotFoundException;
import com.vrx.electronic.store.repository.CartRepository;
import com.vrx.electronic.store.repository.OrderRepository;
import com.vrx.electronic.store.repository.UserRepository;
import com.vrx.electronic.store.service.OrderService;
import com.vrx.electronic.store.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, String userId) {
        Order savedOrder = null;
        //fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        //fetch user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found!"));
        //fetch all the items inside cart
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.size() == 0) {
            throw new BadApiRequest("Invalid number of items in Cart!");
        }
        // prepare the order from order dto
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .orderedDate(new Date())
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderStatus(orderRequest.getOrderStatus())
                .user(user)
                .build();

        AtomicReference<Double> totalOrderAmount = new AtomicReference<>(0.0);
        //take all items inside cart and prepare order items from that
        order.setOrderItems(new ArrayList<>());
        List<OrderItem> orderItemList = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
//            order.getOrderItems().add(orderItem);
            totalOrderAmount.set(totalOrderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).toList();
        //add all prepared items to main order
        order.setOrderItems(orderItemList);
        //set the calculated amount
        order.setOrderAmount(totalOrderAmount.get());
        order.setUser(user);
        //clear the cart so that before placing final order
        cart.getCartItems().clear();
        cartRepository.save(cart);
        //create final order
        savedOrder = orderRepository.save(order);
        logger.info("Saved order: {}", savedOrder);
        return modelMapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        List<Order> orderList = orderRepository.findByUser(user);
        return orderList.stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return PageUtil.getPageableResponse(orderPage, OrderDto.class);
    }

    @Override
    public OrderResponse updateOrderByUser(OrderRequest orderRequest, String orderId) {
        logger.info("orderRequest: {}", orderRequest);
        User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User NOT FOUND!!"));

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order NOT FOUND!!"));

        order.setBillingAddress(orderRequest.getBillingAddress());
        order.setBillingPhone(orderRequest.getBillingPhone());
        order.setBillingName(orderRequest.getBillingName());
        order.setPaymentStatus(orderRequest.getPaymentStatus());
        order.setOrderStatus(orderRequest.getOrderStatus());
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrderByAdmin(OrderRequest orderRequest, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found!"));
        order.setPaymentStatus(orderRequest.getPaymentStatus());
        order.setOrderStatus(orderRequest.getOrderStatus());
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponse.class);
    }
}
