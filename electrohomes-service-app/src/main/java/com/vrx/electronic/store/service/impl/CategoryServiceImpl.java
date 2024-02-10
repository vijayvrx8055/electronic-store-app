package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.dto.CategoryDto;
import com.vrx.electronic.store.dto.ImageResponse;
import com.vrx.electronic.store.dto.PageableResponse;
import com.vrx.electronic.store.entity.Category;
import com.vrx.electronic.store.exception.ResourceNotFoundException;
import com.vrx.electronic.store.repository.CategoryRepository;
import com.vrx.electronic.store.service.CategoryService;
import com.vrx.electronic.store.service.FileService;
import com.vrx.electronic.store.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.cover.image.path}")
    private String coverImagePath;

    @Autowired
    private FileService fileService;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        category.setCategoryId(UUID.randomUUID().toString());
        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Category> page = categoryRepository.findAll(pageable);
        return PageUtil.getPageableResponse(page, CategoryDto.class);
    }

    @Override
    public CategoryDto getById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public ImageResponse uploadCoverImage(MultipartFile file, String categoryId) {
        try {
            String uploadedFile = fileService.uploadFile(file, categoryId);
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category Id is not found !!"));
            category.setCoverImage(uploadedFile);
            categoryRepository.save(category);
            return ImageResponse.builder().imageName(uploadedFile).message("Cover Image uploaded successfully !!")
                    .success(true).status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
