package com.example.campuscart.controllers.ecommerce;

import com.example.campuscart.entities.ecommerce.Order;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.services.ecommerce.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/{accountId}")
    public ResponseEntity<?> fetchOrders(@PathVariable("accountId") Long accountId){
        log.info("Request Received to fetch Orders for accountId: " + accountId);
        try {
            List<Order> response = orderService.fetchMyOrders(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Request to Fetch Orders Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{accountId}/address/{addressId}")
    public ResponseEntity<?> checkout(@PathVariable("accountId") Long accountId, @PathVariable("addressId") Long addressId){
        log.info("Request Received to place order for user: " + accountId + " and address: " + addressId);
        try {
            Order response = orderService.createOrder(accountId, addressId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Request to place order is failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchAllOrders(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size){
        log.info("Request Received to fetch All Orders");
        try {
            Page<Order> response = orderService.fetchAllOrders(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Request to Fetch Orders Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
