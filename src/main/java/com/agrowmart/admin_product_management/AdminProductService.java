package com.agrowmart.admin_product_management;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.agrowmart.dto.auth.product.PendingProductListDTO;
import com.agrowmart.dto.auth.product.ProductResponseDTO;
import com.agrowmart.dto.auth.women.PendingWomenProductDTO;
import com.agrowmart.dto.auth.women.WomenProductResponseDTO;
import com.agrowmart.entity.ApprovalStatus;
import com.agrowmart.entity.Category;
import com.agrowmart.entity.Product;
import com.agrowmart.entity.WomenProduct;
import com.agrowmart.exception.ResourceNotFoundException;
import com.agrowmart.repository.ProductRepository;
import com.agrowmart.repository.WomenProductRepository;
import com.agrowmart.service.ProductService;
import com.agrowmart.service.WomenProductService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminProductService {

    private final ProductRepository productRepository;
    private final WomenProductRepository womenProductRepository;
    private final ProductService productService;
    private final WomenProductService womenProductService;

    public AdminProductService(
            ProductRepository productRepository,
            WomenProductRepository womenProductRepository,
            ProductService productService,
            WomenProductService womenProductService) {

        this.productRepository = productRepository;
        this.womenProductRepository = womenProductRepository;
        this.productService = productService;
        this.womenProductService = womenProductService;
    }

    //================ Regular Products =================

    public List<PendingProductListDTO> getPendingProducts() {
        return productRepository
                .findByApprovalStatusOrderByCreatedAtDesc(ApprovalStatus.PENDING)
                .stream()
                .map(this::toPendingProductDTO)
                .toList();
    }

    public ProductResponseDTO approveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setApprovalStatus(ApprovalStatus.APPROVED);
        product.setStatus(Product.ProductStatus.ACTIVE);
        product.setDeleted(false);

        return productService.toResponseDto(productRepository.save(product));
    }

    public Map<String, Object> rejectProduct(Long productId, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setApprovalStatus(ApprovalStatus.REJECTED);
        product.setStatus(Product.ProductStatus.INACTIVE);

        productRepository.save(product);

        return Map.of(
                "message", "Product rejected successfully",
                "productId", productId,
                "reason", reason != null ? reason : "No reason provided"
        );
    }

    //  SOFT DELETE (NO HARD DELETE)
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setDeleted(true);
        product.setStatus(Product.ProductStatus.INACTIVE);

        productRepository.save(product);
    }

    //  RESTORE PRODUCT
    public ProductResponseDTO restoreProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setDeleted(false);
        product.setStatus(Product.ProductStatus.ACTIVE);

        return productService.toResponseDto(productRepository.save(product));
    }

    // ================ Women Products =====================

    public List<PendingWomenProductDTO> getPendingWomenProducts() {
        return womenProductRepository
                .findByApprovalStatusOrderByCreatedAtDesc(ApprovalStatus.PENDING)
                .stream()
                .map(this::toPendingWomenDTO)
                .toList();
    }

    public WomenProductResponseDTO approveWomenProduct(Long id) {
        WomenProduct product = womenProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Women product not found"));

        product.setApprovalStatus(ApprovalStatus.APPROVED);
        product.setIsAvailable(true);
        product.setDeleted(false);

        return womenProductService.toDTO(womenProductRepository.save(product));
    }

    public Map<String, Object> rejectWomenProduct(Long id, String reason) {
        WomenProduct product = womenProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Women product not found"));

        product.setApprovalStatus(ApprovalStatus.REJECTED);
        product.setIsAvailable(false);

        womenProductRepository.save(product);

        return Map.of(
                "message", "Women product rejected successfully",
                "productId", id,
                "reason", reason != null ? reason : "No reason provided"
        );
    }

    // SOFT DELETE
    public void deleteWomenProduct(Long id) {
        WomenProduct product = womenProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Women product not found"));

        product.setDeleted(true);
        product.setIsAvailable(false);

        womenProductRepository.save(product);
    }

    //  RESTORE
    public WomenProductResponseDTO restoreWomenProduct(Long id) {
        WomenProduct product = womenProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Women product not found"));

        product.setDeleted(false);
        product.setIsAvailable(true);

        return womenProductService.toDTO(womenProductRepository.save(product));
    }

    // ===================== Mapping Helpers ====================

    private PendingProductListDTO toPendingProductDTO(Product p) {
        return new PendingProductListDTO(
                p.getId(),
                p.getProductName(),
                "Merchant " + p.getMerchantId(),
                p.getCategory().getName(),
                p.getCreatedAt() != null ? p.getCreatedAt().toString() : "N/A",
                getImageList(p.getImagePaths()),
                p.getShortDescription(),
                getProductType(p.getCategory())
        );
    }

    private PendingWomenProductDTO toPendingWomenDTO(WomenProduct p) {
        return new PendingWomenProductDTO(
                p.getId(),
                p.getUuid(),
                p.getName(),
                p.getSeller().getId(),
                p.getSeller().getName(),
                p.getCategory(),
                p.getApprovalStatus() != null ? p.getApprovalStatus().name() : "PENDING",
                p.getImageUrlList(),
                p.getMinPrice(),
                p.getMaxPrice(),
                p.getStock(),
                p.getIsAvailable(),
                p.getCreatedAt() != null ? p.getCreatedAt().toString() : "N/A",
                null
        );
    }

    private List<String> getImageList(String paths) {
        if (paths == null || paths.isBlank()) return List.of();
        return Arrays.asList(paths.split(","));
    }

    private String getProductType(Category category) {
        if (category == null) return "Unknown";
        String name = category.getName().toLowerCase();
        if (name.contains("women")) return "Women";
        if (name.contains("men")) return "Men";
        return "Other";
    }
}