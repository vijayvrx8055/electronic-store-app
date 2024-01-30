package com.vrx.electronic.store.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    //error message
    String message() default "{jakarta.validation.constraints.NotBlank.message}";

    // represent group of constraints
    Class<?>[] groups() default { };

    // additional information about annotation
    Class<? extends Payload>[] payload() default { };
}
