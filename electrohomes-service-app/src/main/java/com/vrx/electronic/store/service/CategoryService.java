package com.vrx.electronic.store.service;

import com.vrx.electronic.store.dto.CategoryDto;
import com.vrx.electronic.store.dto.ImageResponse;
import com.vrx.electronic.store.dto.PageableResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {
    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //delete
    void delete(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category details
    CategoryDto getById(String categoryId);

    ImageResponse uploadCoverImage(MultipartFile file, String categoryId);
}
