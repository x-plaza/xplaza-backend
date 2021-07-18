package com.backend.xplaza.service;

import com.backend.xplaza.model.*;
import com.backend.xplaza.repository.CouponDetailsRepository;
import com.backend.xplaza.repository.CouponListRepository;
import com.backend.xplaza.repository.CouponRepository;
import com.backend.xplaza.repository.CouponShopLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepo;
    @Autowired
    private CouponListRepository couponListRepo;
    @Autowired
    private CouponDetailsRepository couponDetailsRepo;
    @Autowired
    private CouponShopLinkRepository couponShopLinkRepo;

    @Transactional
    public void addCoupon(Coupon coupon) {
        couponRepo.save(coupon);
        for (CouponShopLink csl : coupon.getCouponShopLinks()) {
            couponShopLinkRepo.insert(csl.getShop_id(),coupon.getId());
        }
    }

    @Transactional
    public void updateCoupon(Coupon coupon) {
        couponRepo.update(coupon.getAmount(), coupon.getCurrency_id(), coupon.getStart_date(), coupon.getEnd_date(), coupon.getMax_amount(), coupon.getDiscount_type_id(),
                coupon.getIs_active(), coupon.getMin_shopping_amount(), coupon.getId());
        couponShopLinkRepo.deleteByCouponID(coupon.getId());
        for (CouponShopLink csl : coupon.getCouponShopLinks()) {
            couponShopLinkRepo.insert(csl.getShop_id(),coupon.getId());
        }
    }

    public String getCouponNameByID(Long id) {
        return couponRepo.getName(id);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        couponShopLinkRepo.deleteByCouponID(id);
        couponRepo.deleteById(id);
    }

    public List<CouponList> listCoupons() {
        return couponListRepo.findAllCoupons();
    }

    public CouponDetails getCouponDetails(Long id) {
        return couponDetailsRepo.findCouponDetailsById(id);
    }

    public List<CouponList> listCouponsByUserID(Long user_id) {
        return couponListRepo.findCouponsByUserID(user_id);
    }
}
