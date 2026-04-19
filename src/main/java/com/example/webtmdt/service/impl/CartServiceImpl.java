package com.example.webtmdt.service.impl;

import com.example.webtmdt.dto.request.AddToCartRequest;
import com.example.webtmdt.dto.response.CartItemResponse;
import com.example.webtmdt.dto.response.CartResponse;
import com.example.webtmdt.entity.Cart;
import com.example.webtmdt.entity.CartItem;
import com.example.webtmdt.entity.ProductVariant;
import com.example.webtmdt.entity.User;
import com.example.webtmdt.repository.CartItemRepository;
import com.example.webtmdt.repository.CartRepository;
import com.example.webtmdt.repository.ProductImageRepository;
import com.example.webtmdt.repository.ProductVariantRepository;
import com.example.webtmdt.repository.UserRepository;
import com.example.webtmdt.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"));

        Cart cart = cartRepository.findByCustomerId(user.getId()).orElse(null);
        if (cart == null) {
            return CartResponse.builder()
                    .customerId(user.getId())
                    .items(List.of())
                    .build();
        }

        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addToCart(String username, AddToCartRequest request) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"));

        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phân loại sản phẩm"));

        if (!Boolean.TRUE.equals(variant.getActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm này đã ngừng kinh doanh");
        }

        Cart cart = cartRepository.findByCustomerId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().customer(user).build();
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository.findByCartIdAndVariantId(cart.getId(), variant.getId())
                .orElse(null);

        int newQuantity = request.getQuantity();
        if (cartItem != null) {
            newQuantity += cartItem.getQuantity();
        }

        // Soft check số lượng tồn kho
        if (newQuantity > variant.getStockQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Không đủ hàng trong kho. Số lượng hiện có: " + variant.getStockQuantity());
        }

        if (cartItem != null) {
            cartItem.setQuantity(newQuantity);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .variant(variant)
                    .quantity(newQuantity)
                    .build();
            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);
        
        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(String username, Long itemId, Integer quantity) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không hợp lệ"));

        Cart cart = cartRepository.findByCustomerId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Giỏ hàng trống"));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy mục trong giỏ"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền sửa giỏ hàng này");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            cart.getItems().remove(cartItem);
        } else {
            // Soft check tồn kho
            if (quantity > cartItem.getVariant().getStockQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Không đủ hàng trong kho. Số lượng hiện có: " + cartItem.getVariant().getStockQuantity());
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(String username, Long itemId) {
        return updateCartItem(username, itemId, 0);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> {
                    ProductVariant variant = item.getVariant();
                    BigDecimal price = variant.getPriceOverride() != null 
                            ? variant.getPriceOverride() 
                            : variant.getProduct().getBasePrice();

                    String imageUrl = productImageRepository.findByProductIdAndThumbnailTrue(variant.getProduct().getId())
                            .map(img -> img.getImageUrl())
                            .orElse(null);

                    return CartItemResponse.builder()
                            .id(item.getId())
                            .variantId(variant.getId())
                            .productId(variant.getProduct().getId())
                            .productName(variant.getProduct().getName())
                            .color(variant.getColor())
                            .size(variant.getSize())
                            .imageUrl(imageUrl)
                            .unitPrice(price)
                            .quantity(item.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .items(itemResponses)
                .build();
    }
}
