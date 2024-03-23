package com.vrx.electronic.store.controller;

import com.vrx.electronic.store.dto.OrderDto;
import com.vrx.electronic.store.dto.request.OrderRequest;
import com.vrx.electronic.store.dto.response.ApiResponseMessage;
import com.vrx.electronic.store.dto.response.OrderResponse;
import com.vrx.electronic.store.dto.response.PageableResponse;
import com.vrx.electronic.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, @PathVariable String userId) {
        OrderResponse order = orderService.createOrder(orderRequest, userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Order successfully removed!!")
                .success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<OrderDto> pageableResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/users")
    public ResponseEntity<OrderResponse> updateOrderByUser(@RequestBody OrderRequest orderRequest, @PathVariable String orderId) {
        OrderResponse orderResponse = orderService.updateOrderByUser(orderRequest, orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/admin")
    public ResponseEntity<OrderResponse> updateOrderByAdmin(@RequestBody OrderRequest orderRequest, @PathVariable String orderId) {
        OrderResponse orderResponse = orderService.updateOrderByAdmin(orderRequest, orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
