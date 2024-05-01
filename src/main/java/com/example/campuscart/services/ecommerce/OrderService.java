package com.example.campuscart.services.ecommerce;

import com.example.campuscart.entities.ecommerce.*;
import com.example.campuscart.entities.userservice.Address;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.exceptions.OutOfStockException;
import com.example.campuscart.repositories.ecommerce.OrderRepository;
import com.example.campuscart.services.userservice.AddressService;
import com.example.campuscart.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CartService cartService;
    public List<Order> fetchMyOrders(Long accountId) {
        return orderRepository.findOrderByAccountId(accountId);
    }
    public Page<Order> fetchAllOrders(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders;
    }
    //doubt
    public Order createOrder(Long accountId, Long addressId) throws NotFoundException, OutOfStockException{
        User user = userService.getUser(accountId);
        Address address = addressService.getAddress(addressId);
        Cart cart = cartService.getCart(accountId);
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setAddress(address);
        order.setUser(user);
        List<Product> toBeUpdated = new ArrayList<>();
        for(CartItem cartItem: cart.getCartItemList()){
            Product product = cartItem.getProduct();
            Long leftStock = product.getStock() - cartItem.getQuantity();
            if(leftStock<0) {
                throw new OutOfStockException("Product: " + product.getId() + "is out of stock");
            }
            product.setStock(leftStock);
            toBeUpdated.add(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Order createdOrder = orderRepository.save(order);

        for(CartItem cartItem: cart.getCartItemList()){
            cartService.removeFromCart(accountId, cartItem.getProduct().getId());
        }
        for (Product product: toBeUpdated){
            productService.updateStock(product.getId(), product.getStock());
        }
        return createdOrder;
    }
}
