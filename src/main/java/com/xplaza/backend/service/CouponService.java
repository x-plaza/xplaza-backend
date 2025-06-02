/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.common.util.DateConverter;
import com.xplaza.backend.jpa.dao.CouponDao;
import com.xplaza.backend.jpa.repository.CouponRepository;
import com.xplaza.backend.mapper.CouponMapper;
import com.xplaza.backend.service.entity.Coupon;

@Service
public class CouponService extends DateConverter {
  @Autowired
  private CouponRepository couponRepository;
  @Autowired
  private CouponMapper couponMapper;

  @Transactional
  public void addCoupon(Coupon entity) {
    CouponDao dao = couponMapper.toDAO(entity);
    couponRepository.save(dao);
    // handle shop links if needed
  }

  @Transactional
  public void updateCoupon(Coupon entity) {
    CouponDao dao = couponMapper.toDAO(entity);
    couponRepository.save(dao);
    // handle shop links if needed
  }

  public List<Coupon> listCoupons() {
    return couponRepository.findAll().stream().map(couponMapper::toEntityFromDAO).collect(Collectors.toList());
  }

  public Coupon listCoupon(Long id) {
    return couponRepository.findById(id).map(couponMapper::toEntityFromDAO).orElse(null);
  }

  public void deleteCoupon(Long id) {
    couponRepository.deleteById(id);
  }

  public boolean checkCouponValidity(String couponCode, Double netOrderAmount, Long shopId) {
    Optional<Coupon> daoOpt = couponRepository.findAll().stream()
        .filter(dao -> couponCode.equals(dao.getId() != null ? dao.getId().toString() : null))
        .findFirst();
    if (daoOpt.isEmpty())
      return false;
    Coupon coupon = couponMapper.toEntityFromDAO(daoOpt.get());
    // Check active, date, min shopping, shop
    if (coupon.getStartDate() != null && coupon.getEndDate() != null) {
      Date now = new Date();
      if (now.before(coupon.getStartDate()) || now.after(coupon.getEndDate()))
        return false;
    }
    // TODO: Add isActive, minShoppingAmount, shopIds checks if fields exist in
    // CouponEntity
    if (coupon.getShopIds() != null && !coupon.getShopIds().isEmpty() && !coupon.getShopIds().contains(shopId)) {
      return false;
    }
    // Add more checks as needed
    return true;
  }

  public Double calculateCouponAmount(String couponCode, Double netOrderAmount) {
    Optional<Coupon> daoOpt = couponDAORepo.findAll().stream()
        .filter(dao -> couponCode.equals(dao.getId() != null ? dao.getId().toString() : null))
        .findFirst();
    if (daoOpt.isEmpty())
      return 0.0;
    CouponEntity coupon = couponMapper.toEntityFromDAO(daoOpt.get());
    Double amount = coupon.getAmount();
    Double maxAmount = coupon.getMaxAmount();
    Long discountTypeId = coupon.getDiscountTypeId();
    Double calculated = 0.0;
    // Assume discountTypeId==2 means percentage, else fixed
    if (discountTypeId != null && discountTypeId == 2) {
      calculated = (netOrderAmount * amount) / 100.0;
      if (maxAmount != null && calculated > maxAmount)
        calculated = maxAmount;
    } else {
      calculated = amount;
    }
    return calculated;
  }
}
