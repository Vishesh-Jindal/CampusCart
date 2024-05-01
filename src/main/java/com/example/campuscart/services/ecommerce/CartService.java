package com.example.campuscart.services.ecommerce;

import com.example.campuscart.entities.ecommerce.Cart;
import com.example.campuscart.entities.ecommerce.CartItem;
import com.example.campuscart.entities.ecommerce.Product;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.exceptions.OutOfStockException;
import com.example.campuscart.repositories.ecommerce.CartItemRepository;
import com.example.campuscart.repositories.ecommerce.CartRepository;
import com.example.campuscart.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartItemRepository cartItemRepository;
    public Cart getCart(Long accountId) {
        Optional<Cart> optionalCart = cartRepository.findCartByAccountId(accountId);
        if(!optionalCart.isPresent()){
            User user = userService.getUser(accountId);
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
        return optionalCart.get();
    }

    //doubt
    public CartItem addToCart(Long accountId, Long productId) throws NotFoundException, OutOfStockException {
        Product product = productService.fetchProduct(productId);
        if(product.getStock()<=0){
            throw new OutOfStockException("Product: " + productId + " is out of stock");
        }
        Cart cart = this.getCart(accountId);

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setPrice(product.getDiscountedPrice());
        newCartItem.setProduct(product);
        CartItem createdCartItem = cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice()+createdCartItem.getPrice());
        cartRepository.save(cart);

        return createdCartItem;
    }
    public void removeFromCart(Long accountId, Long productId) throws NotFoundException{
        Cart cart = this.getCart(accountId);
        Optional<CartItem> optionalCartItem = cartItemRepository.fetchCartItemByCartAndProduct(cart.getId(), productId);
        if(!optionalCartItem.isPresent()){
            throw new NotFoundException("Product with Id: " + productId + " not found in Cart");
        }
        cart.setTotalPrice(cart.getTotalPrice()-optionalCartItem.get().getPrice());
        cartRepository.save(cart);
        cartItemRepository.delete(optionalCartItem.get());
    }

    public Cart increaseCartItemQuantity(Long cartId, Long cartItemId) throws NotFoundException, OutOfStockException{
        Cart cart = this.getCartByCartId(cartId);
        CartItem cartItem = this.getCartItem(cartItemId);
        Long newQuantity = cartItem.getQuantity() + 1;

        if(cartItem.getProduct().getStock()<(newQuantity)){
            throw new OutOfStockException("Cannot Increase More Quantity as Product: " + cartItem.getProduct().getId() + " has limited stock");
        }

        Double cartTotalPrice = cart.getTotalPrice();
        cartTotalPrice = cartTotalPrice - cartItem.getPrice();

        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice());
        cartItemRepository.save(cartItem);

        cartTotalPrice = cartTotalPrice + cartItem.getPrice();
        cart.setTotalPrice(cartTotalPrice);

        return cartRepository.save(cart);
    }
    public Cart decreaseCartItemQuantity(Long cartId, Long cartItemId) throws NotFoundException{
        Cart cart = this.getCartByCartId(cartId);
        CartItem cartItem = this.getCartItem(cartItemId);

        Double cartTotalPrice = cart.getTotalPrice();
        cartTotalPrice = cartTotalPrice - cartItem.getPrice();

        Long newQuantity = cartItem.getQuantity()-1;
        if(newQuantity == 0) {
            cartItemRepository.delete(cartItem);
            cart.getCartItemList().remove(cartItem);
        }
        else{
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(newQuantity*cartItem.getProduct().getDiscountedPrice());
            cartItemRepository.save(cartItem);
            cartTotalPrice =  cartTotalPrice + cartItem.getPrice();
        }
        cart.setTotalPrice(cartTotalPrice);
        return cartRepository.save(cart);
    }
    public Cart getCartByCartId(Long cartId) throws NotFoundException{
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(!optionalCart.isPresent()){
            throw new NotFoundException("Cart with Id: " + cartId + " not found");
        }
        return optionalCart.get();
    }
    public CartItem getCartItem(Long cartItemId) throws NotFoundException{
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if(!optionalCartItem.isPresent()){
            throw new NotFoundException("CartItem with Id: " + cartItemId + " not found");
        }
        return optionalCartItem.get();
    }
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
