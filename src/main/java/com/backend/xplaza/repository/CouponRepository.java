package com.backend.xplaza.repository;

import com.backend.xplaza.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query(value = "select coupon_code from coupons where coupon_id = ?1", nativeQuery = true)
    String getName(long id);
}