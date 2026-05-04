package com.example.webtmdt.controller;

import com.example.webtmdt.dto.request.TypeVoucherRequest;
import com.example.webtmdt.dto.request.VoucherRequest;
import com.example.webtmdt.dto.response.ApiResponse;
import com.example.webtmdt.dto.response.TypeVoucherResponse;
import com.example.webtmdt.dto.response.VoucherResponse;
import com.example.webtmdt.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    // ==================== PUBLIC / CUSTOMER ====================

    @GetMapping("/api/vouchers/available")
    public ResponseEntity<ApiResponse<List<VoucherResponse>>> getAvailableVouchers() {
        List<VoucherResponse> vouchers = voucherService.getAvailableVouchers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách voucher thành công!", vouchers));
    }

    // ==================== ADMIN ====================

    @PostMapping("/api/admin/vouchers/types")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TypeVoucherResponse>> createTypeVoucher(
            @Valid @RequestBody TypeVoucherRequest request) {
        TypeVoucherResponse type = voucherService.createTypeVoucher(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo loại voucher thành công!", type));
    }

    @GetMapping("/api/admin/vouchers/types")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TypeVoucherResponse>>> getAllTypeVouchers() {
        List<TypeVoucherResponse> types = voucherService.getAllTypeVouchers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách loại voucher thành công!", types));
    }

    @PostMapping("/api/admin/vouchers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(
            @Valid @RequestBody VoucherRequest request) {
        VoucherResponse voucher = voucherService.createVoucher(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo voucher thành công!", voucher));
    }

    @GetMapping("/api/admin/vouchers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<VoucherResponse>>> getAllVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<VoucherResponse> vouchers = voucherService.getAllVouchers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách voucher thành công!", vouchers));
    }

    @DeleteMapping("/api/admin/vouchers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa voucher thành công!", null));
    }
}
