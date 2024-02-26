package com.vrx.electronic.store.repository;

import com.vrx.electronic.store.entity.Order;
import com.vrx.electronic.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
