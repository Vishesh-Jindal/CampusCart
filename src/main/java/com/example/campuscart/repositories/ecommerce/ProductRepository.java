package com.example.campuscart.repositories.ecommerce;

import com.example.campuscart.entities.ecommerce.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.seller.accountId = :accountId")
    public List<Product> getSellerProducts(@Param("accountId") Long accountId);
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isServiceable = true")
    public List<Product> getServiceableCategoryProducts(@Param("categoryId") Long categoryId);
    @Query("UPDATE Product p SET p.stock = :stock WHERE p.id = :productId ")
    public void updateProductStock(@Param("productId") Long productId, @Param("stock") Long stock);
}
