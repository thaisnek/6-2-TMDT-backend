package com.example.webtmdt.service.impl;

import com.example.webtmdt.dto.request.ProductImageRequest;
import com.example.webtmdt.dto.request.ProductRequest;
import com.example.webtmdt.dto.request.ProductVariantRequest;
import com.example.webtmdt.dto.response.ProductImageResponse;
import com.example.webtmdt.dto.response.ProductResponse;
import com.example.webtmdt.dto.response.ProductVariantResponse;
import com.example.webtmdt.entity.*;
import com.example.webtmdt.repository.CategoryRepository;
import com.example.webtmdt.repository.ProductImageRepository;
import com.example.webtmdt.repository.ProductRepository;
import com.example.webtmdt.repository.ProductVariantRepository;
import com.example.webtmdt.repository.SupplierRepository;
import com.example.webtmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;

    // ==================== CREATE ====================

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .brand(request.getBrand())
                .material(request.getMaterial())
                .basePrice(request.getBasePrice())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .build();

        // Set Category
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Không tìm thấy danh mục với id: " + request.getCategoryId()));
            product.setCategory(category);
        }

        // Set Supplier
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Không tìm thấy nhà cung cấp với id: " + request.getSupplierId()));
            product.setSupplier(supplier);
        }

        // Save product first to get ID
        product = productRepository.save(product);

        // Add Variants
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (ProductVariantRequest variantReq : request.getVariants()) {
                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .color(variantReq.getColor())
                        .size(variantReq.getSize())
                        .priceOverride(variantReq.getPriceOverride())
                        .stockQuantity(variantReq.getStockQuantity() != null ? variantReq.getStockQuantity() : 0)
                        .active(variantReq.getActive() != null ? variantReq.getActive() : true)
                        .build();
                product.getVariants().add(variant);
            }
        }

        // Add Images
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (ProductImageRequest imageReq : request.getImages()) {
                ProductImage image = ProductImage.builder()
                        .product(product)
                        .imageUrl(imageReq.getImageUrl())
                        .thumbnail(imageReq.getThumbnail() != null ? imageReq.getThumbnail() : false)
                        .build();
                product.getImages().add(image);
            }
        }

        product = productRepository.save(product);
        return toProductResponse(product);
    }

    // ==================== READ ====================

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy sản phẩm với id: " + id));
        return toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::toProductResponse);
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy sản phẩm với id: " + id));

        // Update basic fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setMaterial(request.getMaterial());
        product.setBasePrice(request.getBasePrice());

        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }

        // Update Category
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Không tìm thấy danh mục với id: " + request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // Update Supplier
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Không tìm thấy nhà cung cấp với id: " + request.getSupplierId()));
            product.setSupplier(supplier);
        } else {
            product.setSupplier(null);
        }

        // Update Variants: xóa cũ, thêm mới
        if (request.getVariants() != null) {
            product.getVariants().clear();
            for (ProductVariantRequest variantReq : request.getVariants()) {
                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .color(variantReq.getColor())
                        .size(variantReq.getSize())
                        .priceOverride(variantReq.getPriceOverride())
                        .stockQuantity(variantReq.getStockQuantity() != null ? variantReq.getStockQuantity() : 0)
                        .active(variantReq.getActive() != null ? variantReq.getActive() : true)
                        .build();
                product.getVariants().add(variant);
            }
        }

        // Update Images: xóa cũ, thêm mới
        if (request.getImages() != null) {
            product.getImages().clear();
            for (ProductImageRequest imageReq : request.getImages()) {
                ProductImage image = ProductImage.builder()
                        .product(product)
                        .imageUrl(imageReq.getImageUrl())
                        .thumbnail(imageReq.getThumbnail() != null ? imageReq.getThumbnail() : false)
                        .build();
                product.getImages().add(image);
            }
        }

        product = productRepository.save(product);
        return toProductResponse(product);
    }

    // ==================== DELETE ====================

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy sản phẩm với id: " + id));
        productRepository.delete(product);
    }

    // ==================== MAPPER ====================

    private ProductResponse toProductResponse(Product product) {
        List<ProductVariantResponse> variantResponses = new ArrayList<>();
        if (product.getVariants() != null) {
            variantResponses = product.getVariants().stream()
                    .map(v -> ProductVariantResponse.builder()
                            .id(v.getId())
                            .color(v.getColor())
                            .size(v.getSize())
                            .priceOverride(v.getPriceOverride())
                            .stockQuantity(v.getStockQuantity())
                            .active(v.getActive())
                            .build())
                    .collect(Collectors.toList());
        }

        List<ProductImageResponse> imageResponses = new ArrayList<>();
        if (product.getImages() != null) {
            imageResponses = product.getImages().stream()
                    .map(i -> ProductImageResponse.builder()
                            .id(i.getId())
                            .imageUrl(i.getImageUrl())
                            .thumbnail(i.getThumbnail())
                            .build())
                    .collect(Collectors.toList());
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .material(product.getMaterial())
                .basePrice(product.getBasePrice())
                .status(product.getStatus())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .supplierId(product.getSupplier() != null ? product.getSupplier().getId() : null)
                .supplierName(product.getSupplier() != null ? product.getSupplier().getName() : null)
                .variants(variantResponses)
                .images(imageResponses)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
