package com.example.campuscart.repositories.ecommerce;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.entities.ecommerce.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(Constants.QueryConstants.FETCH_PRODUCTS_BY_SELLER_ID)
    public List<Product> getSellerProducts(@Param("accountId") Long accountId);
    @Query(Constants.QueryConstants.FETCH_SERVICEABLE_PRODUCTS_BY_CATEGORY)
    public List<Product> getServiceableCategoryProducts(@Param("categoryId") Long categoryId);
}
