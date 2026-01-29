package com.agrowmart.dto.auth.product;

public record ProductSearchDTO(
 String name,
 Long categoryId,
 String status,
 Integer page,
 Integer size
) {}