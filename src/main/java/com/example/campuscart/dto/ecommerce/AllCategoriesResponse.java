package com.example.campuscart.dto.ecommerce;

import com.example.campuscart.entities.ecommerce.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCategoriesResponse {
    private List<Category> categoryList = new ArrayList<>();
}
