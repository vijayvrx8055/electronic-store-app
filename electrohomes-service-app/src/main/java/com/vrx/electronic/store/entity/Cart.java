package com.vrx.electronic.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private String cartId;

    private Date createdOn;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private double totalPrice;


}
