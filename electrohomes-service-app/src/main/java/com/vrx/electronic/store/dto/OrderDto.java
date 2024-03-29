package com.vrx.electronic.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {
    private String orderId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOTPAID";
    private double orderAmount;
    @NotBlank(message = "Address is required")
    private String billingAddress;
    @NotBlank(message = "Phone is required")
    private String billingPhone;
    @NotBlank(message = "Name is required")
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;
    private List<OrderItemDto> orderItems = new ArrayList<>();

}
