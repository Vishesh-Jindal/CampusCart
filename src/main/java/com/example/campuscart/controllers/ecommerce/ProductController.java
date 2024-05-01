package com.example.campuscart.controllers.ecommerce;

import com.example.campuscart.dto.ecommerce.AllCategoryProductsResponse;
import com.example.campuscart.dto.ecommerce.ProductRequest;
import com.example.campuscart.dto.ecommerce.SellerListingResponse;
import com.example.campuscart.entities.ecommerce.Product;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.services.ecommerce.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/product/add/{accountId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addProduct(@PathVariable("accountId") Long accountId, @RequestBody @Valid ProductRequest request, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try{
            Product response = productService.createProduct(accountId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Create Product Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/product/{accountId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> fetchListings(@PathVariable("accountId") Long accountId){
        log.info("Request to fetch Listings of a Seller Received");
        try{
            SellerListingResponse response = productService.fetchSellerListings(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Fetch Listings Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/product/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> editProduct(@PathVariable("productId") Long productId, @RequestBody @Valid ProductRequest request, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try{
            Product response = productService.updateProduct(productId, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Update Product Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/public/product/serviceable/{categoryId}")
    public ResponseEntity<?> fetchCategoryProducts(@PathVariable("categoryId") Long categoryId){
        try{
            AllCategoryProductsResponse response = productService.fetchServiceableCategoryProducts(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Fetch Serviceable Category Products Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/public/product/{productId}")
    public ResponseEntity<?> fetchProduct(@PathVariable("productId") Long productId){
        try{
            Product response = productService.fetchProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Fetch Product Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/product/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchAllProducts(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size){
        try{
            Page<Product> response = productService.fetchAllProducts(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Fetch All Products Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/product/{productId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveProduct(@PathVariable("productId") Long productId){
        try{
            Product response = productService.approveProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Approve Product Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
