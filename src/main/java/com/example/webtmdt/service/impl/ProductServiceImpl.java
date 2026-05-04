package com.example.webtmdt.service.impl;

import com.example.webtmdt.dto.request.ProductImageRequest;
import com.example.webtmdt.dto.request.ProductRequest;
import com.example.webtmdt.dto.request.ProductVariantRequest;
import com.example.webtmdt.dto.response.ProductResponse;
import com.example.webtmdt.entity.*;
import com.example.webtmdt.exception.ResourceNotFoundException;
import com.example.webtmdt.mapper.ProductMapper;
import com.example.webtmdt.repository.CategoryRepository;
import com.example.webtmdt.repository.ProductRepository;
import com.example.webtmdt.repository.SupplierRepository;
import com.example.webtmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

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
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        // Set Supplier
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nhà cung cấp", "id", request.getSupplierId()));
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
        return productMapper.toResponse(product);
    }

    // ==================== READ ====================

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsBySupplier(Long supplierId, Pageable pageable) {
        return productRepository.findBySupplierId(supplierId, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> filterByPrice(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable)
                .map(productMapper::toResponse);
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);

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
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "id", request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // Update Supplier
        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nhà cung cấp", "id", request.getSupplierId()));
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
        return productMapper.toResponse(product);
    }

    // ==================== DELETE ====================

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    // ==================== HELPER ====================

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "id", id));
    }
}
