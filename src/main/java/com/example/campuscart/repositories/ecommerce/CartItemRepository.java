package com.example.campuscart.repositories.ecommerce;

import com.example.campuscart.entities.ecommerce.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ct FROM CartItem ct WHERE ct.cart.id = :cartId AND ct.product.id = :productId")
    public Optional<CartItem> fetchCartItemByCartAndProduct(@Param("cartId") Long cartId, @Param("productId") Long productId);
}
