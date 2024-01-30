package com.vrx.electronic.store.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

    Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public void initialize(ImageNameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    // contains logic of our custom validator
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.isBlank()) {
            return false;
        } else {
            logger.info("Image Name is valid: {}", value);
            return true;
        }
    }
}
