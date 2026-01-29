package com.agrowmart.controller;

import com.agrowmart.dto.auth.women.WomenProductCreateDTO;
import com.agrowmart.dto.auth.women.WomenProductResponseDTO;
import com.agrowmart.dto.auth.women.WomenProductStatusRequest;
import com.agrowmart.entity.User;
import com.agrowmart.service.WomenProductService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/women-products")
public class WomenProductController {

    private final WomenProductService service;

    public WomenProductController(WomenProductService service) {
        this.service = service;
    }

    // ========================= CREATE PRODUCT =========================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('WOMEN')")
    public ResponseEntity<WomenProductResponseDTO> create(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam(required = false) String description,
          
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(required = false) Integer stock,
            @RequestParam String unit,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            
            @RequestParam(required = false) String ingredients,
            @RequestParam(required = false) String shelfLife,
            @RequestParam(required = false) String packagingType,
            @RequestParam(required = false) String productInfo
    ) throws Exception {

        WomenProductCreateDTO dto = new WomenProductCreateDTO(
                name,
                category,
                description,
               
                minPrice,
                maxPrice,
                stock,
                unit,
                ingredients,
                shelfLife,
                packagingType,
                productInfo
        );

        WomenProductResponseDTO response = service.createProduct(
                user.getId(),
                dto,
                images != null ? images : List.of()
        );

        return ResponseEntity.ok(response);
    }

    // ========================= GET MY PRODUCTS =========================
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('WOMEN')")
    public List<WomenProductResponseDTO> getMyProducts(@AuthenticationPrincipal User user) {
        return service.getMyProducts(user.getId());
    }

    // ========================= GET ALL PRODUCTS =========================
    @GetMapping
    public List<WomenProductResponseDTO> getAll() {
        return service.getAllWomenProducts();
    }

    // ========================= GET SINGLE PRODUCT =========================
    @GetMapping("/{id}")
    public WomenProductResponseDTO getById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    // ========================= UPDATE PRODUCT =========================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('WOMEN')")
    public ResponseEntity<WomenProductResponseDTO> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam(required = false) String description,
        
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(required = false) Integer stock,
            @RequestParam String unit,
            @RequestParam(value = "images", required = false) List<MultipartFile> images, // Fixed: added this
            @RequestParam(required = false) String ingredients,
            @RequestParam(required = false) String shelfLife,
            @RequestParam(required = false) String packagingType,
            @RequestParam(required = false) String productInfo
    ) throws Exception {

        WomenProductCreateDTO dto = new WomenProductCreateDTO(
                name,
                category,
                description,
                
                minPrice,
                maxPrice,
                stock,
                unit,
                ingredients,
                shelfLife,
                packagingType,
                productInfo
        );

        WomenProductResponseDTO updated = service.updateProduct(
                user.getId(),
                id,
                dto,
                images != null ? images : List.of()
        );

        return ResponseEntity.ok(updated);
    }

    // ========================= DELETE =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WOMEN')")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.deleteProduct(user.getId(), id);
        return ResponseEntity.ok("Product deleted successfully");
    }
    
    
  //-- status
 // ========================= CHANGE PRODUCT STATUS (ACTIVE / INACTIVE) =========================
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('WOMEN')")
    public ResponseEntity<WomenProductResponseDTO> updateProductStatus(
            @PathVariable Long id,
            @RequestBody @Valid WomenProductStatusRequest request,
            @AuthenticationPrincipal User user) {

        boolean isActive = "ACTIVE".equalsIgnoreCase(request.status());

        WomenProductResponseDTO updated = service.updateWomenProductStatus(
                id,
                isActive,
                user.getId()
        );

        return ResponseEntity.ok(updated);
    }
}