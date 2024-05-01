package com.example.campuscart.dto.ecommerce;

import com.example.campuscart.entities.ecommerce.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerListingResponse {
    private Long sellerId;
    private List<Product> listings = new ArrayList<>();
}
