package com.example.campuscart.services.ecommerce;

import com.example.campuscart.dto.ecommerce.AllCategoryProductsResponse;
import com.example.campuscart.dto.ecommerce.ProductRequest;
import com.example.campuscart.dto.ecommerce.SellerListingResponse;
import com.example.campuscart.entities.ecommerce.Category;
import com.example.campuscart.entities.ecommerce.Product;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.repositories.ecommerce.ProductRepository;
import com.example.campuscart.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    public Product createProduct(Long accountId, ProductRequest request) throws NotFoundException{
        User seller = userService.getUser(accountId);
        Category category = categoryService.getCategory(request.getCategoryId());
        Product newProduct = new Product();
        newProduct.setCategory(category);
        newProduct.setCondition(request.getCondition());
        newProduct.setSeller(seller);
        newProduct.setAttributes(request.getAttributes());
        newProduct.setName(request.getName());
        newProduct.setDescription(request.getDescription());
        newProduct.setDiscountedPrice(request.getDiscountedPrice());
        newProduct.setMrp(request.getMrp());
        newProduct.setImageUrl(request.getImageUrl());
        newProduct.setStock(request.getStock());

        Product createdProduct = productRepository.save(newProduct);
        return createdProduct;
    }
    public SellerListingResponse fetchSellerListings(Long accountId) throws NotFoundException {
        List<Product> productList = productRepository.getSellerProducts(accountId);
        return SellerListingResponse.builder()
                .sellerId(accountId)
                .listings(productList)
                .build();
    }
    public Product updateProduct(Long productId, ProductRequest request) throws NotFoundException {
        Product existingProduct = this.fetchProduct(productId);
        if(existingProduct.getCategory().getId() != request.getCategoryId()){
            Category category = categoryService.getCategory(request.getCategoryId());
            existingProduct.setCategory(category);
        }
        existingProduct.setCondition(request.getCondition());
        existingProduct.setAttributes(request.getAttributes());
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setDiscountedPrice(request.getDiscountedPrice());
        existingProduct.setMrp(request.getMrp());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setStock(request.getStock());
        existingProduct.setIsServiceable(false);

        Product updatedProduct = productRepository.save(existingProduct);
        return updatedProduct;
    }
    public AllCategoryProductsResponse fetchServiceableCategoryProducts(Long categoryId) throws NotFoundException{
        List<Product> productList = productRepository.getServiceableCategoryProducts(categoryId);
        return AllCategoryProductsResponse.builder()
                .categoryId(categoryId)
                .productList(productList)
                .build();
    }
    public Product fetchProduct(Long productId) throws NotFoundException{
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(!optionalProduct.isPresent()){
            throw new NotFoundException("Product with Id: " + productId + " not found");
        }
        Product product = optionalProduct.get();
        return product;
    }
    public Page<Product> fetchAllProducts(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return products;
    }
    public Product approveProduct(Long productId) throws NotFoundException {
        Product product = this.fetchProduct(productId);
        product.setIsServiceable(true);
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }
    public Product updateStock(Long productId, Long stock){
        Product product = this.fetchProduct(productId);
        product.setStock(stock);
        return productRepository.save(product);
    }
}
