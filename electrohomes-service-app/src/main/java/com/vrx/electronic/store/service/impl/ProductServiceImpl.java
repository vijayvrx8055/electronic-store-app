package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.dto.ImageResponse;
import com.vrx.electronic.store.dto.PageableResponse;
import com.vrx.electronic.store.dto.ProductDto;
import com.vrx.electronic.store.entity.Category;
import com.vrx.electronic.store.entity.Product;
import com.vrx.electronic.store.exception.ResourceNotFoundException;
import com.vrx.electronic.store.repository.CategoryRepository;
import com.vrx.electronic.store.repository.ProductRepository;
import com.vrx.electronic.store.service.FileService;
import com.vrx.electronic.store.service.ProductService;
import com.vrx.electronic.store.util.PageUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.image.path}")
    private String productImagePath;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with given ID !!"));
        product.setDescription(productDto.getDescription());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setInStock(productDto.isInStock());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(productDto.getAddedDate());
        product.setTitle(productDto.getTitle());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        Product saved = productRepository.save(product);
        return mapper.map(saved, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        deleteProductImage(productId);
        productRepository.delete(product);
    }

    public void deleteProductImage(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found!!"));
        String fullPath = productImagePath + product.getProductImageName();
        Path path = Paths.get(fullPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize,
                                                       String sortBy, String sortOrder) {
        Sort sort = (sortOrder.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findAll(pageable);
        return PageUtil.getPageableResponse(products, ProductDto.class);

    }

    @Override
    public ProductDto getProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize,
                                                   String sortBy, String sortOrder) {
        Sort sort = (sortOrder.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByIsLive(true, pageable);
        return PageUtil.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchProducts(String subTitle, int pageNumber, int pageSize,
                                                       String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByTitleContaining(subTitle, pageable);
        return PageUtil.getPageableResponse(products, ProductDto.class);
    }

    @Override
    public ImageResponse uploadImage(MultipartFile file, String productId) {

        try {
            String uploadedFile = fileService.uploadFile(file, productImagePath);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product NOT FOUND !!"));
            product.setProductImageName(uploadedFile);
            productRepository.save(product);
            return ImageResponse.builder().imageName(uploadedFile)
                    .status(HttpStatus.CREATED)
                    .success(true)
                    .message("Product Image Uploaded Successfully !!")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void serveProductImage(String productId, HttpServletResponse response) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product NOT FOUND !!"));
        try {
            InputStream resource = fileService.getResource(productImagePath, product.getProductImageName());
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        //fetch the category from DB
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found !!"));
        Product product = mapper.map(productDto, Product.class);
        //save the category inside product
        product.setCategory(category);
        //generate random productID
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        Product saved = productRepository.save(product);
        return mapper.map(saved, ProductDto.class);
    }
}
