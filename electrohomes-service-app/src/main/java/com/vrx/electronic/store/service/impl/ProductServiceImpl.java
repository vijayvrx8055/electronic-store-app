package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.dto.PageableResponse;
import com.vrx.electronic.store.dto.ProductDto;
import com.vrx.electronic.store.entity.Product;
import com.vrx.electronic.store.exception.ResourceNotFoundException;
import com.vrx.electronic.store.repository.ProductRepository;
import com.vrx.electronic.store.service.ProductService;
import com.vrx.electronic.store.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

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
        productRepository.delete(product);
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
}
