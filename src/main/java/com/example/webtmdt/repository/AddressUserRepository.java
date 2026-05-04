package com.example.webtmdt.repository;

import com.example.webtmdt.entity.AddressUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressUserRepository extends JpaRepository<AddressUser, Long> {

    List<AddressUser> findByUserId(Long userId);

    Optional<AddressUser> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserId(Long userId);
}
