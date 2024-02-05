package com.vrx.electronic.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "category_title", length = 60)
    private String title;
    @Column(name = "category_desc", length = 500)
    private String description;
    @Column(name = "category_cov_img")
    private String coverImage;

}
