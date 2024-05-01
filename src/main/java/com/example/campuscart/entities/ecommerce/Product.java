package com.example.campuscart.entities.ecommerce;

import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.enums.ProductState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "display_name")
    private String name;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "attributes", columnDefinition = "JSON")
    private String attributes;
    @Column(name = "description")
    private String description;
    @Column(name = "stock")
    private Long stock;
    @Column(name = "mrp")
    private Double mrp;
    @Column(name = "discount_price")
    private Double discountedPrice;
    @Column(name = "product_condition")
    @Enumerated(EnumType.STRING)
    private ProductState condition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private User seller;
    @Column(name = "serviceable")
    private Boolean isServiceable = false;
}
