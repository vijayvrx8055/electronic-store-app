package com.vrx.electronic.store.dto.response;

import com.vrx.electronic.store.dto.OrderItemDto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderResponse {
    private String orderId;
    private String orderStatus;
    private String paymentStatus;
    private double orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;
    private List<OrderItemDto> orderItems = new ArrayList<>();
// always use DTO objects to avoid infinite recursion
}
