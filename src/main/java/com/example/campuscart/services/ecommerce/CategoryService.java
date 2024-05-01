package com.example.campuscart.services.ecommerce;

import com.example.campuscart.dto.ecommerce.AllCategoriesResponse;
import com.example.campuscart.dto.ecommerce.CategoryRequest;
import com.example.campuscart.dto.ecommerce.CategoryResponse;
import com.example.campuscart.entities.ecommerce.Category;
import com.example.campuscart.exceptions.AlreadyExistsException;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.repositories.ecommerce.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) throws AlreadyExistsException{
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryRequest.getName().toLowerCase());
        if(optionalCategory.isPresent()){
            throw new AlreadyExistsException("Category: " + categoryRequest.getName() + " already exists");
        }
        Category newCategory = new Category();
        newCategory.setName(categoryRequest.getName().toLowerCase());
        Category createdCategory = categoryRepository.save(newCategory);
        return CategoryResponse.builder()
                .id(createdCategory.getId())
                .name(createdCategory.getName())
                .build();
    }
    public AllCategoriesResponse getAllCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return AllCategoriesResponse.builder()
                .categoryList(categoryList)
                .build();
    }
    public Category getCategory(Long categoryId) throws NotFoundException{
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(!optionalCategory.isPresent()){
            throw new NotFoundException("Category: " + categoryId + " not found");
        }
        return optionalCategory.get();
    }
}
