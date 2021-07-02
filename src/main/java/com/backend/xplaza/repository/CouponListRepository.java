package com.backend.xplaza.repository;

import com.backend.xplaza.model.CouponList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponListRepository extends JpaRepository<CouponList, Long> {
    @Query(value = "select cou.*, c.currency_name, dt.discount_type_name " +
            "from coupons cou " +
            "left join currencies c on cou.fk_currency_id = c.currency_id " +
            "left join discount_types dt on dt.discount_type_id = cou.fk_discount_type_id", nativeQuery = true)
    List<CouponList> findAllCoupons();

    @Query(value = "select cou.*, c.currency_name, dt.discount_type_name " +
            "from coupons cou " +
            "left join currencies c on cou.fk_currency_id = c.currency_id " +
            "left join discount_types dt on dt.discount_type_id = cou.fk_discount_type_id " +
            "where cou.coupon_id = ?1", nativeQuery = true)
    CouponList findCouponById(long id);
}
