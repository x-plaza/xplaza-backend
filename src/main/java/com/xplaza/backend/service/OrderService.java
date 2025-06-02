/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.ProductDiscount;
import com.xplaza.backend.jpa.repository.AdminUserRepository;
import com.xplaza.backend.jpa.repository.CouponDetailsRepository;
import com.xplaza.backend.jpa.repository.CurrencyRepository;
import com.xplaza.backend.jpa.repository.CustomerUserRepository;
import com.xplaza.backend.jpa.repository.DeliveryCostListRepository;
import com.xplaza.backend.jpa.repository.OrderDAORepository;
import com.xplaza.backend.jpa.repository.OrderDetailsRepository;
import com.xplaza.backend.jpa.repository.OrderListRepository;
import com.xplaza.backend.jpa.repository.ProductDiscountRepository;
import com.xplaza.backend.jpa.repository.ProductRepository;
import com.xplaza.backend.mapper.OrderMapper;
import com.xplaza.backend.model.CouponDetails;
import com.xplaza.backend.model.CouponShopList;
import com.xplaza.backend.model.DeliveryCostList;
import com.xplaza.backend.model.OrderDetails;
import com.xplaza.backend.model.OrderList;
import com.xplaza.backend.model.OrderResponse;
import com.xplaza.backend.model.PlatformInfo;
import com.xplaza.backend.model.Product;
import com.xplaza.backend.model.ProductInventory;
import com.xplaza.backend.service.entity.OrderEntity;
import com.xplaza.backend.service.entity.OrderItemEntity;

@Service
public class OrderService {
  @Autowired
  private OrderDAORepository orderDAORepo;
  @Autowired
  private OrderMapper orderMapper;
  @Autowired
  private OrderListRepository orderListRepo;
  @Autowired
  private OrderDetailsRepository orderDetailsRepo;
  @Autowired
  private ProductRepository productRepo;
  @Autowired
  private ProductDiscountRepository productDiscountRepo;
  @Autowired
  private CouponDetailsRepository couponDetailsRepo;
  @Autowired
  private DeliveryCostListRepository deliveryCostListRepo;
  @Autowired
  private EmailSenderService emailSenderService;
  @Autowired
  private CustomerUserRepository customerUserRepo;
  @Autowired
  private AdminUserRepository adminUserRepo;
  @Autowired
  private CurrencyRepository currencyRepo;
  @Autowired
  private Environment env;

  @Transactional
  public ProductInventory checkProductAvailability(OrderEntity order) {
    ProductInventory productInventory = new ProductInventory();
    productInventory.set_available(true);
    for (OrderItemEntity item : order.getOrderItemList()) {
      Product product = productRepo.findProductById(item.getProductId());
      if (product.getQuantity() < item.getQuantity()) {
        productInventory.setId(product.getId());
        productInventory.setName(product.getName());
        productInventory.setMax_available_quantity(product.getQuantity());
        productInventory.set_available(false);
        break;
      }
    }
    return productInventory;
  }

  @Transactional
  public OrderEntity setOrderPrices(OrderEntity order) {
    Double total_price = 0.0;
    Double total_discount = 0.0;
    Double net_total = 0.0;
    for (OrderItemEntity item : order.getOrderItemList()) {
      Product product = productRepo.findProductById(item.getProductId());
      Double original_selling_price = product.getSelling_price();
      Double buying_price = product.getBuying_price();
      Double discount_amount = 0.0;
      ProductDiscount productDiscount = productDiscountRepo.findByProductId(item.getProductId());
      if (productDiscount != null) {
        discount_amount = productDiscount.getDiscountAmount();
        Long discount_type = productDiscount.getDiscountTypeId();
        if (discount_type == 2) {
          discount_amount = original_selling_price * (discount_amount / 100);
        }
      }
      Double unit_price = original_selling_price - discount_amount;
      item.setUnitPrice(unit_price);
      item.setProductSellingPrice(original_selling_price);
      item.setProductBuyingPrice(buying_price);
      item.setItemTotalPrice(unit_price * item.getQuantity());
      total_price += original_selling_price * item.getQuantity();
      net_total += item.getItemTotalPrice();
      item.setQuantityType("pc");
    }
    total_discount = total_price - net_total;
    order.setTotalPrice(total_price);
    order.setDiscountAmount(total_discount);
    order.setNetTotal(net_total);
    Double delivery_cost = 0.0;
    if (order.getDeliveryCostId() != null) {
      DeliveryCostList dc = deliveryCostListRepo.findDeliveryCostById(order.getDeliveryCostId());
      delivery_cost = dc.getCost();
    }
    order.setDeliveryCost(delivery_cost);
    Double coupon_amount = 0.0;
    if (order.getCouponId() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCouponId());
      if (couponDetails.getDiscount_type_name().equals("Fixed Amount"))
        coupon_amount = couponDetails.getAmount();
      else {
        coupon_amount = (order.getNetTotal() * couponDetails.getAmount()) / 100;
        if (couponDetails.getMax_amount() != null && coupon_amount > couponDetails.getMax_amount())
          coupon_amount = couponDetails.getMax_amount();
      }
    } else if (order.getCouponCode() != null) {
      CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCouponCode());
      if (couponDetails.getDiscount_type_name().equals("Fixed Amount"))
        coupon_amount = couponDetails.getAmount();
      else {
        coupon_amount = (order.getNetTotal() * couponDetails.getAmount()) / 100;
        if (couponDetails.getMax_amount() != null && coupon_amount > couponDetails.getMax_amount())
          coupon_amount = couponDetails.getMax_amount();
      }
    }
    order.setCouponAmount(coupon_amount);
    order.setGrandTotalPrice(net_total + delivery_cost - coupon_amount);
    order.setPaymentTypeId(1L);
    return order;
  }

  @Transactional
  public boolean checkCouponValidity(OrderEntity order, String type) throws ParseException {
    CouponDetails couponDetails = null;
    if (type.equals("Code"))
      couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCouponCode());
    else
      couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCouponId());
    Date received_time = order.getReceivedTime();
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    if (!(received_time.compareTo(formatter.parse(couponDetails.getStart_date())) >= 0
        && received_time.compareTo(formatter.parse(couponDetails.getEnd_date())) <= 0))
      return false;
    if (!couponDetails.getIs_active())
      return false;
    if (order.getNetTotal() < couponDetails.getMin_shopping_amount())
      return false;
    boolean is_valid = false;
    for (CouponShopList shop : couponDetails.getShopList()) {
      if (Objects.equals(shop.getShop_id(), order.getShopId())) {
        is_valid = true;
        break;
      }
    }
    return is_valid;
  }

  @Transactional
  public OrderEntity addOrder(OrderEntity order) {
    Order dao = orderMapper.toDAO(order);
    dao = orderDAORepo.save(dao);
    return orderMapper.toEntityFromDAO(dao);
  }

  @Transactional
  public void updateOrder(OrderEntity order) {
    Order dao = orderMapper.toDAO(order);
    orderDAORepo.save(dao);
  }

  @Transactional
  public void updateOrderStatus(Long orderId, String remarks, Long status) {
    // Implement as needed in DAO repo
  }

  public void deleteOrder(Long id) {
    orderDAORepo.deleteById(id);
  }

  // For a master admin
  public List<OrderList> listOrdersByAdmin() {
    return orderListRepo.findAllOrdersByAdmin();
  }

  public List<OrderList> listOrdersByStatusByAdmin(String status) {
    return orderListRepo.findAllOrdersByStatusByAdmin(status);
  }

  public List<OrderList> listOrdersByFilterByAdmin(String status, Date order_date) {
    return orderListRepo.findAllOrdersByFilterByAdmin(status, order_date);
  }

  // For a normal admin
  public List<OrderList> listOrdersByAdminUserID(Long user_id) {
    return orderListRepo.findAllOrdersAdminUserID(user_id);
  }

  public List<OrderList> listOrdersByStatusAndAdminUserID(String status, Long user_id) {
    return orderListRepo.findAllOrdersByStatusAndAdminUserID(status, user_id);
  }

  public List<OrderList> listOrdersByFilterAndAdminUserID(String status, Date order_date, Long user_id) {
    return orderListRepo.findAllOrdersByFilterAndAdminUserID(status, order_date, user_id);
  }

  // For any admin user
  public OrderDetails listOrderDetails(Long id) {
    return orderDetailsRepo.findOrderDetails(id);
  }

  // For any customer user
  public List<OrderEntity> listOrdersByCustomer(Long customerId) {
    // TODO: Implement with DAO repository and mapping
    return new ArrayList<>();
  }

  public List<OrderEntity> listOrdersByStatusByCustomer(Long customerId, String status) {
    // TODO: Implement with DAO repository and mapping
    return new ArrayList<>();
  }

  public List<OrderEntity> listOrdersByFilterByCustomer(Long customerId, String status, Date orderDate) {
    // TODO: Implement with DAO repository and mapping
    return new ArrayList<>();
  }

  public void sendOrderDetailsToCustomer(OrderEntity order, OrderResponse dtos, PlatformInfo platformInfo) {
    // get currency
    String currency_name = currencyRepo.getName(order.getCurrencyId());

    // format date and time
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
    String delivery_date = dateFormatter.format(order.getDateToDeliver());
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    String delivery_schedule = timeFormatter.format(order.getDeliveryScheduleStart()) + " - " +
        timeFormatter.format(order.getDeliveryScheduleEnd());

    // formatter for decimal to 2 places
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);

    // send email to customer
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    String email = customerUserRepo.getUsername(order.getCustomerId());
    mailMessage.setFrom(env.getProperty("spring.mail.username"));
    mailMessage.setTo(email);
    mailMessage.setSubject("Your " + platformInfo.getName() + ".com Order.");
    mailMessage.setText("Dear " + order.getCustomerName() + ",\n\n" +
        "Thank you for your order. We’ll let you know once your item(s) have dispatched.\n\n" +
        "The summary of your order is as follows:\n\n" +
        "Order No : " + dtos.getInvoice_number() + "\n" +
        "Grand Total : " + nf.format(dtos.getGrand_total_price()) + " " + currency_name + "\n" +
        "Delivery Date : " + delivery_date + "\n" +
        "Delivery Schedule : " + delivery_schedule + "\n" +
        "Delivery Address : " + order.getDeliveryAddress() + "\n\n" +
        "You can view the details of your order by visiting My Orders on https://"
        + platformInfo.getName().toLowerCase() + ".com.\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName());
    emailSenderService.sendEmail(mailMessage);
  }

  public void sendOrderDetailsToShopAdmin(OrderEntity order, OrderResponse dtos, PlatformInfo platformInfo) {
    // get currency
    String currency_name = currencyRepo.getName(order.getCurrencyId());

    // format date and time
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
    String delivery_date = dateFormatter.format(order.getDateToDeliver());
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    String delivery_schedule = timeFormatter.format(order.getDeliveryScheduleStart()) + " - " +
        timeFormatter.format(order.getDeliveryScheduleEnd());

    // formatter for decimal to 2 places
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);

    // send email to shop admins
    List<String> emailList = adminUserRepo.getEmailList(order.getShopId());
    for (String email : emailList) {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      if (email.equals("admin@gmail.com"))
        continue;
      else {
        mailMessage.setFrom(env.getProperty("spring.mail.username"));
        mailMessage.setTo(email);
        mailMessage.setSubject(platformInfo.getName() + ".com Customer Order.");
        mailMessage.setText("Hello,\n\n" +
            "The following order has been placed by the customer: " + order.getCustomerName() + ".\n\n" +
            "You can view the order details by visiting Pending Orders on https://admin."
            + platformInfo.getName().toLowerCase() + ".com.\n\n" +
            "Order No : " + dtos.getInvoice_number() + "\n" +
            "Grand Total : " + nf.format(dtos.getGrand_total_price()) + " " + currency_name + "\n" +
            "Delivery Date : " + delivery_date + "\n" +
            "Delivery Schedule : " + delivery_schedule + "\n" +
            "Delivery Address : " + order.getDeliveryAddress() + "\n\n" +
            "With Regards,\n" + "Team " + platformInfo.getName());
        emailSenderService.sendEmail(mailMessage);
      }
    }
  }
}
