package com.vrx.electronic.store.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddItemToCartRequest {

    private String productId;
    private int quantity;
}
