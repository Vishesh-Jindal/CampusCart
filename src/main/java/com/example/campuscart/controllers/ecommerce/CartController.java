package com.example.campuscart.controllers.ecommerce;

import com.example.campuscart.entities.ecommerce.Cart;
import com.example.campuscart.entities.ecommerce.CartItem;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.exceptions.OutOfStockException;
import com.example.campuscart.services.ecommerce.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/{accountId}/addProduct/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable("accountId") Long accountId, @PathVariable("productId") Long productId){
        log.info("Request Received to add item:" + productId + " to cart for accountId: "+ accountId);
        try{
            CartItem response = cartService.addToCart(accountId, productId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (OutOfStockException outOfStockException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(outOfStockException.getMessage());
        } catch (Exception e){
            log.error("Add to Cart Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{accountId}/removeProduct/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable("accountId") Long accountId, @PathVariable("productId") Long productId){
        log.info("Request Received to remove item:" + productId + " from cart for accountId: "+ accountId);
        try{
            cartService.removeFromCart(accountId, productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Remove from Cart Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{cartId}/cartItem/{cartItemId}/increase")
    public ResponseEntity<?> increaseQuantity(@PathVariable("cartId") Long cartId, @PathVariable("cartItemId") Long cartItemId){
        log.info("Request Received to increase quantity of cartItem:" + cartItemId + " in cart: "+ cartId);
        try{
            Cart response = cartService.increaseCartItemQuantity(cartId, cartItemId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (OutOfStockException outOfStockException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(outOfStockException.getMessage());
        } catch (Exception e){
            log.error("Increase Quantity of CartItem Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{cartId}/cartItem/{cartItemId}/decrease")
    public ResponseEntity<?> decreaseQuantity(@PathVariable("cartId") Long cartId, @PathVariable("cartItemId") Long cartItemId){
        log.info("Request Received to decrease quantity of cartItem:" + cartItemId + " in cart: "+ cartId);
        try{
            Cart response = cartService.decreaseCartItemQuantity(cartId, cartItemId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Decrease Quantity of CartItem Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{accountId}")
    public ResponseEntity<?> fetchCart(@PathVariable("accountId") Long accountId){
        log.info("Request Received to fetch Cart for accountId: " + accountId);
        try{
            Cart response = cartService.getCart(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Fetch Cart Request Failed due to: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
