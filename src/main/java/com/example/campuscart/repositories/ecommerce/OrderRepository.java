package com.example.campuscart.repositories.ecommerce;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.entities.ecommerce.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(Constants.QueryConstants.FETCH_ORDER_BY_ACCOUNT_ID)
    public List<Order> findOrderByAccountId(@Param("accountId") Long accountId);
}
