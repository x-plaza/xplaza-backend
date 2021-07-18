package com.backend.xplaza.repository;

import com.backend.xplaza.model.CouponDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CouponDetailsRepository extends JpaRepository<CouponDetails, Long> {
    @Query(value = "select cou.*, cur.currency_name, cur.currency_sign, dt.discount_type_name "+
            "from coupons cou left join currencies cur on cou.fk_currency_id = cur.currency_id "+
            "left join discount_types dt on dt.discount_type_id = cou.fk_discount_type_id " +
            "where cou.coupon_id = ?1", nativeQuery = true)
    CouponDetails findCouponDetailsById(Long id);
}
