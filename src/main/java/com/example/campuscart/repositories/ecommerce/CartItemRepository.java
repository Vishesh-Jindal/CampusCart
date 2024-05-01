package com.example.campuscart.repositories.ecommerce;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.entities.ecommerce.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(Constants.QueryConstants.FETCH_CARTITEM_BY_CART_AND_PRODUCT)
    public Optional<CartItem> fetchCartItemByCartAndProduct(@Param("cartId") Long cartId, @Param("productId") Long productId);
}
