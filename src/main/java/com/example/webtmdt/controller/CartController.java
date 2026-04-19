package com.example.webtmdt.controller;

import com.example.webtmdt.dto.request.AddToCartRequest;
import com.example.webtmdt.dto.response.ApiResponse;
import com.example.webtmdt.dto.response.CartResponse;
import com.example.webtmdt.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        CartResponse cart = cartService.getCart(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Lấy giỏ hàng thành công!", cart));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddToCartRequest request) {
        CartResponse cart = cartService.addToCart(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Đã thêm vào giỏ hàng!", cart));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        CartResponse cart = cartService.updateCartItem(userDetails.getUsername(), itemId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật giỏ hàng thành công!", cart));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        CartResponse cart = cartService.removeCartItem(userDetails.getUsername(), itemId);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa sản phẩm khỏi giỏ hàng!", cart));
    }
}
