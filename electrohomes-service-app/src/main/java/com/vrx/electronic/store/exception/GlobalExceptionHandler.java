package com.vrx.electronic.store.exception;

import com.vrx.electronic.store.dto.response.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException Invoked !!!! : {}", ex.getMessage());
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException Invoked !!!! : {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        allErrors.stream().forEach(err -> {
            String message = err.getDefaultMessage();
            String field = ((FieldError) err).getField();
            response.put(field, message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequestException(BadApiRequest ex) {
        logger.error("Bad API Request !!");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);

    }
}
