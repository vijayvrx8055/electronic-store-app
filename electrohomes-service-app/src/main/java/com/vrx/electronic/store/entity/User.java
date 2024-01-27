package com.vrx.electronic.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
public class User {

    @Id
    private String userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", length = 10, nullable = false)
    private String password;

    @Column(name = "user_gender", nullable = false)
    private String gender;

    @Column(name = "user_desc", length = 500)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;


}
