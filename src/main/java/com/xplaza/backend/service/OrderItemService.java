/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.OrderItem;
import com.xplaza.backend.jpa.dao.ProductDiscount;
import com.xplaza.backend.jpa.repository.CouponDetailsRepository;
import com.xplaza.backend.jpa.repository.DeliveryCostRepository;
import com.xplaza.backend.jpa.repository.OrderItemRepository;
import com.xplaza.backend.jpa.repository.OrderRepository;
import com.xplaza.backend.jpa.repository.ProductDiscountRepository;
import com.xplaza.backend.jpa.repository.ProductRepository;
import com.xplaza.backend.mapper.OrderItemMapper;
import com.xplaza.backend.model.CouponDetails;
import com.xplaza.backend.model.DeliveryCost;
import com.xplaza.backend.model.Order;
import com.xplaza.backend.model.Product;
import com.xplaza.backend.service.entity.OrderItemEntity;

@Service
public class OrderItemService {
  @Autowired
  private OrderItemRepository orderItemRepo;
  @Autowired
  private OrderRepository orderRepo;
  @Autowired
  private ProductRepository productRepo;
  @Autowired
  private DeliveryCostRepository deliveryCostRepo;
  @Autowired
  private CouponDetailsRepository couponDetailsRepo;
  @Autowired
  private ProductDiscountRepository productDiscountRepo;
  @Autowired
  private OrderItemMapper orderItemMapper;

  @Transactional
  public void addOrderItem(OrderItemEntity orderItemEntity) {
    OrderItem dao = orderItemMapper.toDAO(orderItemEntity);
    Order order = orderRepo.findOrderById(dao.getOrderId());
    Product product = productRepo.findProductById(dao.getProductId());
    Double originalSellingPrice = product.getSelling_price();
    Double buyingPrice = product.getBuying_price();
    dao.setProductSellingPrice(originalSellingPrice);
    dao.setProductBuyingPrice(buyingPrice);

    Double discountAmount = 0.0;
    ProductDiscount productDiscount = productDiscountRepo.findByProductId(dao.getProductId());
    if (productDiscount != null) {
      discountAmount = productDiscount.getDiscountAmount();
      Long discountType = productDiscount.getDiscountTypeId();
      if (discountType == 2) {
        discountAmount = originalSellingPrice * (discountAmount / 100);
      }
    }
    Double unitPrice = originalSellingPrice - discountAmount;
    dao.setUnitPrice(unitPrice);
    dao.setItemTotalPrice(dao.getUnitPrice() * dao.getQuantity());
    Double itemTotalPrice = dao.getItemTotalPrice();

    Double netTotal = order.getNet_total();
    Double totalPrice = order.getTotal_price();

    netTotal += itemTotalPrice;
    totalPrice += originalSellingPrice * dao.getQuantity();
    Double totalDiscount = totalPrice - netTotal;

    Double deliveryCost = order.getDelivery_cost();
    List<DeliveryCost> dcList = deliveryCostRepo.findAll();
    for (DeliveryCost dc : dcList) {
      if (netTotal >= dc.getStart_range() && netTotal <= dc.getEnd_range())
        deliveryCost = dc.getCost();
    }

    Double couponAmount = order.getCoupon_amount();
    if (order.getCoupon_id() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
      if (Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
        couponAmount = (order.getNet_total() * couponDetails.getAmount()) / 100;
        if (couponAmount > couponDetails.getMax_amount())
          couponAmount = couponDetails.getMax_amount();
      }
    } else if (order.getCoupon_code() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
      if (Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
        couponAmount = (order.getNet_total() * couponDetails.getAmount()) / 100;
        if (couponAmount > couponDetails.getMax_amount())
          couponAmount = couponDetails.getMax_amount();
      }
    }
    Double grandTotal = netTotal + deliveryCost - couponAmount;
    order.setTotal_price(totalPrice);
    order.setNet_total(netTotal);
    order.setDelivery_cost(deliveryCost);
    order.setDiscount_amount(totalDiscount);
    order.setCoupon_amount(couponAmount);
    order.setGrand_total_price(grandTotal);

    dao.setQuantityType("pc");

    orderItemRepo.save(dao);
    orderRepo.save(order);

    Long originalQuantity = product.getQuantity();
    Long updatedQuantity = originalQuantity - dao.getQuantity();
    productRepo.updateProductInventory(product.getId(), updatedQuantity);
  }

  public List<OrderItemEntity> listOrderItems() {
    return orderItemRepo.findAll().stream().map(orderItemMapper::toEntityFromDAO).toList();
  }

  public OrderItemEntity listOrderItem(Long id) {
    OrderItem dao = orderItemRepo.findOrderItemById(id);
    return orderItemMapper.toEntityFromDAO(dao);
  }

  public String getOrderItemNameByID(Long id) {
    return orderItemRepo.getName(id);
  }

  @Transactional
  public void deleteOrderItem(Long id) {
    OrderItem dao = orderItemRepo.findOrderItemById(id);
    OrderItemEntity entity = orderItemMapper.toEntityFromDAO(dao);
    Order order = orderRepo.findOrderById(entity.getOrderId());

    Double originalPrice = entity.getProductSellingPrice();
    Long oldQuantity = entity.getQuantity();
    Double unitPrice = entity.getUnitPrice();
    Double oldItemTotalPrice = entity.getItemTotalPrice();
    Double totalPrice = order.getTotal_price();
    Double netTotal = order.getNet_total();

    Double newItemTotalPrice = unitPrice * 0;
    netTotal = netTotal - oldItemTotalPrice + newItemTotalPrice;
    totalPrice = totalPrice - (oldQuantity * originalPrice);
    Double totalDiscount = totalPrice - netTotal;

    Double deliveryCost = order.getDelivery_cost();
    List<DeliveryCost> dcList = deliveryCostRepo.findAll();
    for (DeliveryCost dc : dcList) {
      if (netTotal >= dc.getStart_range() && netTotal <= dc.getEnd_range())
        deliveryCost = dc.getCost();
    }

    Double couponAmount = order.getCoupon_amount();
    if (order.getCoupon_id() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
      if (Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
        couponAmount = (order.getNet_total() * couponDetails.getAmount()) / 100;
        if (couponAmount > couponDetails.getMax_amount())
          couponAmount = couponDetails.getMax_amount();
      }
    } else if (order.getCoupon_code() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
      if (Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
        couponAmount = (order.getNet_total() * couponDetails.getAmount()) / 100;
        if (couponAmount > couponDetails.getMax_amount())
          couponAmount = couponDetails.getMax_amount();
      }
    }
    Double grandTotal = netTotal + deliveryCost - couponAmount;

    order.setTotal_price(totalPrice);
    order.setNet_total(netTotal);
    order.setDelivery_cost(deliveryCost);
    order.setDiscount_amount(totalDiscount);
    order.setCoupon_amount(couponAmount);
    order.setGrand_total_price(grandTotal);
    entity.setItemTotalPrice(newItemTotalPrice);
    entity.setQuantity(0L);
    orderItemRepo.save(orderItemMapper.toDAO(entity));
    orderRepo.save(order);

    Product product = productRepo.findProductById(entity.getProductId());
    if (product != null) {
      Long originalQuantity = product.getQuantity();
      Long updatedQuantity = originalQuantity - entity.getQuantity();
      productRepo.updateProductInventory(product.getId(), updatedQuantity);
    }
  }

  @Transactional
  public void updateOrderItem(Long orderItemId, Long newQuantity) {
    OrderItem dao = orderItemRepo.findOrderItemById(orderItemId);
    OrderItemEntity entity = orderItemMapper.toEntityFromDAO(dao);
    Long oldQuantity = entity.getQuantity();
    entity.setQuantity(newQuantity);
    Order order = orderRepo.findOrderById(entity.getOrderId());

    Double originalPrice = entity.getProductSellingPrice();
    Double unitPrice = entity.getUnitPrice();
    Double oldItemTotalPrice = entity.getItemTotalPrice();
    Double totalPrice = order.getTotal_price();
    Double netTotal = order.getNet_total();

    Double newItemTotalPrice = unitPrice * newQuantity;
    netTotal = netTotal - oldItemTotalPrice + newItemTotalPrice;
    totalPrice = totalPrice - (oldQuantity * originalPrice) + (newQuantity * originalPrice);
    Double totalDiscount = totalPrice - netTotal;

    Double deliveryCost = order.getDelivery_cost();
    List<DeliveryCost> dcList = deliveryCostRepo.findAll();
    for (DeliveryCost dc : dcList) {
      if (netTotal >= dc.getStart_range() && netTotal <= dc.getEnd_range())
        deliveryCost = dc.getCost();
    }

    Double couponAmount = order.getCoupon_amount();
    if (order.getCoupon_id() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
      if (Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
        couponAmount = (order.getNet_total() * couponDetails.getAmount()) / 100;
        if (couponAmount > couponDetails.getMax_amount())
          couponAmount = couponDetails.getMax_amount();
      }
    } else if (order.getCoupon_code() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
      if (Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
        couponAmount = (order.getNet_total() * couponDetails.getAmount()) / 100;
        if (couponAmount > couponDetails.getMax_amount())
          couponAmount = couponDetails.getMax_amount();
      }
    }
    Double grandTotal = netTotal + deliveryCost - couponAmount;

    order.setTotal_price(totalPrice);
    order.setNet_total(netTotal);
    order.setDelivery_cost(deliveryCost);
    order.setDiscount_amount(totalDiscount);
    order.setCoupon_amount(couponAmount);
    order.setGrand_total_price(grandTotal);
    entity.setItemTotalPrice(newItemTotalPrice);
    entity.setQuantity(newQuantity);
    entity.setQuantityType("pc");

    orderItemRepo.save(orderItemMapper.toDAO(entity));
    orderRepo.save(order);

    Product product = productRepo.findProductById(entity.getProductId());
    if (product != null) {
      Long originalQuantity = product.getQuantity();
      Long updatedQuantity = originalQuantity - entity.getQuantity();
      productRepo.updateProductInventory(product.getId(), updatedQuantity);
    }
  }
}
