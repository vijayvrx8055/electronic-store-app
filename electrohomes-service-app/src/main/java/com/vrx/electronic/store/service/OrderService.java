package com.vrx.electronic.store.service;

import com.vrx.electronic.store.dto.OrderDto;
import com.vrx.electronic.store.dto.request.OrderRequest;
import com.vrx.electronic.store.dto.response.OrderResponse;
import com.vrx.electronic.store.dto.response.PageableResponse;
import com.vrx.electronic.store.entity.Order;

import java.util.List;

public interface OrderService {

    //place order
    OrderResponse createOrder(OrderRequest orderRequest, String userId);

    //remove order
    void removeOrder(String orderId);

    //get orders or user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    // update orders by user
    OrderResponse updateOrderByUser(OrderRequest orderRequest,String orderId);

    //update orders by admin
    OrderResponse updateOrderByAdmin(OrderRequest orderRequest, String orderId);


}
