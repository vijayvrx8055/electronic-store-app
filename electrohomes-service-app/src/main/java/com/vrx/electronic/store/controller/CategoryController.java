package com.vrx.electronic.store.controller;

import com.vrx.electronic.store.dto.response.ApiResponseMessage;
import com.vrx.electronic.store.dto.CategoryDto;
import com.vrx.electronic.store.dto.response.ImageResponse;
import com.vrx.electronic.store.dto.response.PageableResponse;
import com.vrx.electronic.store.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto response = categoryService.create(categoryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        CategoryDto response = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().success(true)
                .status(HttpStatus.OK)
                .message("Category is deleted !!")
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<CategoryDto> response = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        CategoryDto response = categoryService.getById(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCoverImage(@RequestParam MultipartFile file, @PathVariable String categoryId) {
        ImageResponse imageResponse = categoryService.uploadCoverImage(file, categoryId);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }
}
