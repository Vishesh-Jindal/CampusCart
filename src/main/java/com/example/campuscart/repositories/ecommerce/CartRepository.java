package com.example.campuscart.repositories.ecommerce;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.entities.ecommerce.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(Constants.QueryConstants.FETCH_CART_BY_ACCOUNT_ID)
    public Optional<Cart> findCartByAccountId(@Param("accountId") Long accountId);
}
