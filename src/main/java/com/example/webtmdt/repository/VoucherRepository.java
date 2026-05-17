package com.example.webtmdt.repository;

import com.example.webtmdt.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findByCodeVoucher(String codeVoucher);

    /** Lấy danh sách voucher còn hiệu lực và còn số lượng */
    @Query("SELECT v FROM Voucher v WHERE v.fromDate <= :now AND v.toDate >= :now AND v.usedQuantity < v.quantity")
    List<Voucher> findAvailableVouchers(LocalDateTime now);

    @Modifying
    @Query("UPDATE Voucher v SET v.usedQuantity = v.usedQuantity + 1 WHERE v.id = :id AND v.usedQuantity < v.quantity")
    int incrementUsedQuantity(@Param("id") Long id);
}
