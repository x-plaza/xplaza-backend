package com.backend.xplaza.service;

import com.backend.xplaza.model.Coupon;
import com.backend.xplaza.model.CouponList;
import com.backend.xplaza.repository.CouponListRepository;
import com.backend.xplaza.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepo;
    @Autowired
    private CouponListRepository couponListRepo;

    public void addCoupon(Coupon coupon) {
        couponRepo.save(coupon);
    }

    public void updateCoupon(Coupon coupon) {
        couponRepo.save(coupon);
    }

    public String getCouponNameByID(long id) {
        return couponRepo.getName(id);
    }

    public void deleteCoupon(long id) {
        couponRepo.deleteById(id);
    }

    public List<CouponList> listCoupons() {
        return couponListRepo.findAllCoupons();
    }

    public CouponList listCoupon(long id) {
        return couponListRepo.findCouponById(id);
    }
}
