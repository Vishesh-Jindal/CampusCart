package com.example.campuscart.entities.ecommerce;

import com.example.campuscart.entities.userservice.Address;
import com.example.campuscart.entities.userservice.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private User user;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "order_date")
    private LocalDate orderDate;
}