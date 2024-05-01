package com.example.campuscart.dto.ecommerce;


import com.example.campuscart.enums.ProductState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String imageUrl;
    @NotEmpty
    private String attributes;
    @NotEmpty
    private String description;
    @NotNull
    private Long stock;
    @NotNull
    private Double mrp;
    @NotNull
    private Double discountedPrice;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductState condition;
    @NotNull
    private Long categoryId;
}
