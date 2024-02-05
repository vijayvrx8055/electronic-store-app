package com.vrx.electronic.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotNull(message = "Title is required !!")
    @NotBlank(message = "Title is required !!")
    @Size(min = 4, message = "Minimum length of title should be 4 characters !!")
    private String title;

    @NotNull(message = "Description is required !!")
    @NotBlank(message = "Description is required !!")
    private String description;

    private String coverImage;

}
