package com.backend.xplaza.service;

import com.backend.xplaza.model.*;
import com.backend.xplaza.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderListRepository orderListRepo;
    @Autowired
    private OrderDetailsRepository orderDetailsRepo;
    @Autowired
    private OrderPlaceRepository orderPlaceRepo;
    @Autowired
    private OrderPlaceListRepository orderPlaceListRepo;
    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductDiscountRepository productDiscountRepo;
    @Autowired
    private CouponDetailsRepository couponDetailsRepo;
    @Autowired
    private DeliveryCostListRepository deliveryCostListRepo;

    @Transactional
    public OrderPlace setOrderPrices (OrderPlace order){
        Double total_price = 0.0;
        Double total_discount = 0.0;
        for (OrderItem item : order.getOrderItemList()) {
            Product product = productRepo.findProductById(item.getProduct_id());
            Double original_price = product.getSelling_price();
            Double discount_amount = productDiscountRepo.findProductDiscountByProductId(item.getProduct_id());
            Double unit_price = original_price - discount_amount;
            item.setUnit_price(unit_price);
            item.setItem_total_price(unit_price * item.getQuantity());
            total_price += original_price;
            total_discount += discount_amount;
        }
        order.setTotal_price(total_price);
        order.setDiscount_amount(total_discount);
        order.setNet_total(total_price-total_discount); // sum of item total for an order

        Double delivery_cost = 0.0;
        if(order.getDelivery_cost_id() != null){
            DeliveryCostList dc = deliveryCostListRepo.findDeliveryCostById(order.getDelivery_cost_id());
            delivery_cost = dc.getCost();
        }
        order.setDelivery_cost(delivery_cost);

        Double coupon_amount = 0.0;
        if(order.getCoupon_id() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
            if(couponDetails.getDiscount_type_name() == "Fixed Amount") coupon_amount= couponDetails.getAmount();
            else { // for percentage
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        order.setCoupon_amount(coupon_amount);
        order.setGrand_total_price(order.getNet_total() + delivery_cost - coupon_amount);
        return order;
    }

    public boolean checkCouponValidity(OrderPlace order) throws ParseException {

        CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
        Date received_time = order.getReceived_time();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        if (!(received_time.compareTo(formatter.parse(couponDetails.getStart_date())) >= 0
                && received_time.compareTo(formatter.parse(couponDetails.getEnd_date())) <= 0))
            return false;
        if (!couponDetails.getIs_active())  return false;
        if (order.getNet_total() < couponDetails.getMin_shopping_amount()) return false;
        return true;
    }

    @Transactional
    public void addOrder(OrderPlace order) {
        order = orderPlaceRepo.save(order);
        for (OrderItem oi : order.getOrderItemList()) {
            oi.setOrder_id(order.getInvoice_number());
            orderItemRepo.save(oi);
        }
    }

    @Transactional
    public void updateOrder(OrderPlace order) {
        order = orderPlaceRepo.save(order);
        for (OrderItem oi : order.getOrderItemList()) {
            orderItemRepo.save(oi);
        }
    }

    public void updateOrderStatus(Long order_id, String remarks, Long status) {
        orderRepo.updateOrderStatus(order_id,remarks,status);
    }

    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    // For a master admin
    public List<OrderList> listOrdersByAdmin() { return orderListRepo.findAllOrdersByAdmin(); }

    public List<OrderList> listOrdersByStatusByAdmin(String status) {
        return orderListRepo.findAllOrdersByStatusByAdmin(status);
    }

    public List<OrderList> listOrdersByFilterByAdmin(String status, Date order_date) {
        return orderListRepo.findAllOrdersByFilterByAdmin(status,order_date);
    }

    // For a normal admin
    public List<OrderList> listOrdersByAdminUserID(Long user_id) {
        return orderListRepo.findAllOrdersAdminUserID(user_id);
    }

    public List<OrderList> listOrdersByStatusAndAdminUserID(String status, Long user_id) {
        return orderListRepo.findAllOrdersByStatusAndAdminUserID(status,user_id);
    }

    public List<OrderList> listOrdersByFilterAndAdminUserID(String status, Date order_date, Long user_id) {
        return orderListRepo.findAllOrdersByFilterAndAdminUserID(status,order_date,user_id);
    }

    // For any admin user
    public OrderDetails listOrderDetailsByAdmin(Long id) { return orderDetailsRepo.findOrderDetailsByIdByAdmin(id); }

    // For any customer user
    public List<OrderPlaceList> listOrdersByCustomer(Long customer_id) { return orderPlaceListRepo.findAllOrdersByCustomer(customer_id); }

    public List<OrderPlaceList> listOrdersByStatusByCustomer(Long customer_id, String status) {
        return orderPlaceListRepo.findAllOrdersByStatusByCustomer(customer_id,status);
    }

    public List<OrderPlaceList> listOrdersByFilterByCustomer(Long customer_id, String status, Date order_date) {
        return orderPlaceListRepo.findAllOrdersByFilterByCustomer(customer_id,status,order_date);
    }
}
