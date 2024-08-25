package com.vrx.electronic.store.dto;


import com.vrx.electronic.store.entity.Role;
import com.vrx.electronic.store.validator.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min = 3, max = 15, message = "Invalid Name !!")
    private String name;

    //    @Email(message = "Invalid Email !!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,3}$", message = "Invalid Email Format !!")
    @NotBlank(message = "Email is required !!")
    private String email;
    
    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid Gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    //Custom validator
    @ImageNameValid(message = "Image name is Invalid !!")
    private String imageName;

    private Set<RoleDto> roles;
}
