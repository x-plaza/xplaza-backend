/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.xplaza.common.DateConverter;
import com.backend.xplaza.model.*;
import com.backend.xplaza.repository.CouponDetailsRepository;
import com.backend.xplaza.repository.CouponListRepository;
import com.backend.xplaza.repository.CouponRepository;
import com.backend.xplaza.repository.CouponShopLinkRepository;

@Service
public class CouponService extends DateConverter {
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
      couponShopLinkRepo.insert(csl.getShop_id(), coupon.getId());
    }
  }

  @Transactional
  public void updateCoupon(Coupon coupon) {
    couponRepo.update(coupon.getAmount(), coupon.getCurrency_id(), coupon.getStart_date(), coupon.getEnd_date(),
        coupon.getMax_amount(), coupon.getDiscount_type_id(),
        coupon.getIs_active(), coupon.getMin_shopping_amount(), coupon.getId());
    couponShopLinkRepo.deleteByCouponID(coupon.getId());
    for (CouponShopLink csl : coupon.getCouponShopLinks()) {
      couponShopLinkRepo.insert(csl.getShop_id(), coupon.getId());
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

  public boolean isExist(Coupon coupon) {
    return couponRepo.existsByName(coupon.getCoupon_code());
  }

  @Transactional
  public boolean checkCouponValidity(String coupon_code, Double net_order_amount, Long shop_id) throws ParseException {
    if (!couponRepo.existsByName(coupon_code))
      return false;
    CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(coupon_code);
    Date received_time = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    if (!(received_time.compareTo(formatter.parse(couponDetails.getStart_date())) >= 0
        && received_time.compareTo(formatter.parse(couponDetails.getEnd_date())) <= 0))
      return false;
    if (!couponDetails.getIs_active())
      return false;
    if (couponDetails.getMin_shopping_amount() != null) {
      if (net_order_amount < couponDetails.getMin_shopping_amount())
        return false;
    }
    // check if coupon is available for this shop?
    boolean is_valid = false;
    for (CouponShopList shop : couponDetails.getShopList()) {
      if (Objects.equals(shop.getShop_id(), shop_id)) {
        is_valid = true;
        break;
      }
    }
    return is_valid;
  }

  @Transactional
  public Double calculateCouponAmount(String coupon_code, Double net_order_amount) {
    Double coupon_amount = 0.0;
    CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(coupon_code);
    if (couponDetails.getDiscount_type_name().equals("Fixed Amount"))
      coupon_amount = couponDetails.getAmount();
    else { // for percentage
      coupon_amount = (net_order_amount * couponDetails.getAmount()) / 100;
      if (couponDetails.getMax_amount() != null) {
        if (coupon_amount > couponDetails.getMax_amount())
          coupon_amount = couponDetails.getMax_amount();
      }
    }
    return coupon_amount;
  }

  public boolean checkCouponDateValidity(Coupon coupon) {
    Date current_date = new Date();
    current_date = convertDateToStartOfTheDay(current_date);
    Date start_date = coupon.getStart_date();
    Date end_date = coupon.getEnd_date();
    if (current_date.after(start_date))
      return false;
    if (current_date.after(end_date))
      return false;
    if (coupon.getStart_date().after(coupon.getEnd_date()))
      return false;
    return true;
  }
}
