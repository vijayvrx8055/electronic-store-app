package com.vrx.electronic.store.controller;

import com.vrx.electronic.store.dto.ApiResponseMessage;
import com.vrx.electronic.store.dto.ImageResponse;
import com.vrx.electronic.store.dto.PageableResponse;
import com.vrx.electronic.store.dto.ProductDto;
import com.vrx.electronic.store.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto response = productService.create(productDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto updated = productService.update(productDto, productId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product is deleted successfully !!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    //get product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        ProductDto product = productService.getProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //get products: live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse> getALlLiveProducts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                               @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                               @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> product = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //search products
    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse> searchProducts(@PathVariable String subTitle, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> product = productService.searchProducts(subTitle, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile file, @PathVariable String productId) {
        ImageResponse imageResponse = productService.uploadImage(file, productId);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response){
        productService.serveProductImage(productId,response);

    }


}
