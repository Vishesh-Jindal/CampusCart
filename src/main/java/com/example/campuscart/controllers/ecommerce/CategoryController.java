package com.example.campuscart.controllers.ecommerce;

import com.example.campuscart.dto.ecommerce.AllCategoriesResponse;
import com.example.campuscart.dto.ecommerce.CategoryRequest;
import com.example.campuscart.dto.ecommerce.CategoryResponse;
import com.example.campuscart.exceptions.AlreadyExistsException;
import com.example.campuscart.services.ecommerce.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request, BindingResult bindingResult){
        log.info("Request Received to create category: "+ request.getName());
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try {
            CategoryResponse response = categoryService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AlreadyExistsException alreadyExistsException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(alreadyExistsException.getMessage());
        } catch (Exception e){
            log.error("Create Category Request Failed due to: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/public/category/all")
    public ResponseEntity<?> fetchAllCategories(){
        log.info("Request Received to fetch all categories");
        try {
            AllCategoriesResponse response = categoryService.getAllCategories();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Fetch All Categories Request Failed due to: "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
