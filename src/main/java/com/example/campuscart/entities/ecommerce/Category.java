package com.example.campuscart.entities.ecommerce;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "category", uniqueConstraints = @UniqueConstraint(columnNames = {"category_name"}))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "category_name")
    private String name;
}
