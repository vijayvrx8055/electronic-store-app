package com.vrx.electronic.store.repository;

import com.vrx.electronic.store.entity.Category;
import com.vrx.electronic.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

    Page<Product> findByIsLive(Boolean isLive, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);
}
