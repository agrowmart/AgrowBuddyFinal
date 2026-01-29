package com.agrowmart.dto.auth.product;

import java.util.List;

public record ProductResponseDTO(
    Long id,
    String productName,
    String shortDescription,
    String status,
    Long categoryId,
    String categoryName,
    List<String> imageUrls,
    Long merchantId,
    String productType, // VEGETABLE, DAIRY, MEAT
    Object details,    // Full detail object
    
   String stockStatus,
 
   Long serialNo
) {}