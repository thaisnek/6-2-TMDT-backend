package com.example.webtmdt.repository;

import com.example.webtmdt.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findByCodeVoucher(String codeVoucher);

    /** Lấy danh sách voucher còn hiệu lực */
    @Query("SELECT v FROM Voucher v WHERE v.fromDate <= :now AND v.toDate >= :now")
    List<Voucher> findAvailableVouchers(LocalDateTime now);
}
