package com.backend.xplaza.service;

import com.backend.xplaza.model.*;
import com.backend.xplaza.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

    @Transactional
    public void addOrderItem(OrderItem orderItem) {
        Order order = orderRepo.findOrderById(orderItem.getOrder_id());
        Product product = productRepo.findProductById(orderItem.getProduct_id());
        Double original_price = product.getSelling_price();
        orderItem.setProduct_selling_price(original_price);

        // check discount amount with validity and discount type
        Double discount_amount = 0.0;
        ProductDiscount productDiscount = productDiscountRepo.findByProductId(orderItem.getProduct_id());
        if(productDiscount != null)
        {
            discount_amount = productDiscount.getDiscount_amount();
            Long discount_type = productDiscount.getDiscount_type_id();
            if(discount_type == 2) // Percentage
            {
                discount_amount = original_price * (discount_amount/100);
            }
        }
        Double unit_price = original_price - discount_amount; // here unit price is basically the discounted price
        orderItem.setUnit_price(unit_price);
        orderItem.setItem_total_price(orderItem.getUnit_price() * orderItem.getQuantity());
        Double item_total_price = orderItem.getItem_total_price();

        Double net_total = order.getNet_total();
        Double total_price = order.getTotal_price();

        net_total += item_total_price;
        total_price += original_price * orderItem.getQuantity();
        Double total_discount = total_price - net_total;

        // Check updated delivery cost
        Double delivery_cost = order.getDelivery_cost();
        List<DeliveryCost> dcList = deliveryCostRepo.findAll();
        for (DeliveryCost dc: dcList)
        {
            if(net_total >= dc.getStart_range() && net_total <= dc.getEnd_range())
                delivery_cost = dc.getCost();
        }

        // Check updated coupon value
        Double coupon_amount = order.getCoupon_amount();
        if(order.getCoupon_id() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
            if(Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        else if(order.getCoupon_code() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
            if(Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        Double grand_total = net_total + delivery_cost - coupon_amount;
        order.setTotal_price(total_price);
        order.setNet_total(net_total);
        order.setDelivery_cost(delivery_cost);
        order.setDiscount_amount(total_discount);
        order.setCoupon_amount(coupon_amount);
        order.setGrand_total_price(grand_total);

        // set order item quantity type
        orderItem.setQuantity_type("pc"); // fixed it since it will always be pc.

        orderItemRepo.save(orderItem);
        orderRepo.save(order);

        // Update product inventory
        Long original_quantity = product.getQuantity();
        Long updated_quantity = original_quantity - orderItem.getQuantity();
        productRepo.updateProductInventory(product.getId(), updated_quantity);
    }

    public List<OrderItem> listOrderItems() { return orderItemRepo.findAll(); }

    public OrderItem listOrderItem(Long id) { return orderItemRepo.findOrderItemById(id); }

    public String getOrderItemNameByID(Long id) {
        return orderItemRepo.getName(id);
    }

    @Transactional
    public void deleteOrderItem(Long id) {
        // This approach is basically instead of deleting the product, I am making quantity to zero.
        long new_quantity = 0;
        OrderItem item = orderItemRepo.findOrderItemById(id);
        Order order = orderRepo.findOrderById(item.getOrder_id());

        // Get the old values:
        // Double original_price = product.getSelling_price();
        Double original_price = item.getProduct_selling_price();
        Long old_quantity = item.getQuantity();
        Double unit_price = item.getUnit_price();
        Double old_item_total_price = item.getItem_total_price();
        Double total_price = order.getTotal_price();
        Double net_total = order.getNet_total();

        // Calculate the new values:
        Double new_item_total_price = unit_price * new_quantity;
        net_total = net_total - old_item_total_price + new_item_total_price;
        total_price = total_price - (old_quantity * original_price) + (new_quantity * original_price);
        Double total_discount = total_price - net_total;

        // Check updated delivery cost
        Double delivery_cost = order.getDelivery_cost();
        List<DeliveryCost> dcList = deliveryCostRepo.findAll();
        for (DeliveryCost dc: dcList)
        {
            if(net_total >= dc.getStart_range() && net_total <= dc.getEnd_range()) delivery_cost = dc.getCost();
        }

        // Check updated coupon value
        Double coupon_amount = order.getCoupon_amount();
        if(order.getCoupon_id() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
            if(Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        else if(order.getCoupon_code() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
            if(Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        Double grand_total = net_total + delivery_cost - coupon_amount;

        // Set the new values and save them:
        order.setTotal_price(total_price);
        order.setNet_total(net_total);
        order.setDelivery_cost(delivery_cost);
        order.setDiscount_amount(total_discount);
        order.setCoupon_amount(coupon_amount);
        order.setGrand_total_price(grand_total);
        item.setItem_total_price(new_item_total_price);
        item.setQuantity(new_quantity);
        orderItemRepo.save(item);
        orderRepo.save(order);

        // Update product inventory
        Product product = productRepo.findProductById(item.getProduct_id());
        if (product != null) {
            Long original_quantity = product.getQuantity();
            Long updated_quantity = original_quantity - item.getQuantity();
            productRepo.updateProductInventory(product.getId(), updated_quantity);
        }
    }

    @Transactional
    public void updateOrderItem(Long order_item_id, Long new_quantity) {
        OrderItem item = orderItemRepo.findOrderItemById(order_item_id);
        Order order = orderRepo.findOrderById(item.getOrder_id());

        // Get the old values:
        Double original_price = item.getProduct_selling_price();
        Long old_quantity = item.getQuantity();
        Double unit_price = item.getUnit_price(); // discount amount ta alada kore check korinai, sorasori ager item er unit price ta niye nisi
        Double old_item_total_price = item.getItem_total_price();
        Double total_price = order.getTotal_price();
        Double net_total = order.getNet_total();

        // Calculate the new values:
        Double new_item_total_price = unit_price * new_quantity;
        net_total = net_total - old_item_total_price + new_item_total_price;
        total_price = total_price - (old_quantity * original_price) + (new_quantity * original_price);
        Double total_discount = total_price - net_total;

        // Check updated delivery cost
        Double delivery_cost = order.getDelivery_cost();
        List<DeliveryCost> dcList = deliveryCostRepo.findAll();
        for (DeliveryCost dc: dcList)
        {
            if(net_total >= dc.getStart_range() && net_total <= dc.getEnd_range()) delivery_cost = dc.getCost();
        }

        // Check updated coupon value
        Double coupon_amount = order.getCoupon_amount();
        if(order.getCoupon_id() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
            if(Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        else if(order.getCoupon_code() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
            if(Objects.equals(couponDetails.getDiscount_type_name(), "Percentage")) {
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(coupon_amount > couponDetails.getMax_amount())
                    coupon_amount = couponDetails.getMax_amount();
            }
        }
        Double grand_total = net_total + delivery_cost - coupon_amount;

        // Set the new values and save them:
        order.setTotal_price(total_price);
        order.setNet_total(net_total);
        order.setDelivery_cost(delivery_cost);
        order.setDiscount_amount(total_discount);
        order.setCoupon_amount(coupon_amount);
        order.setGrand_total_price(grand_total);
        item.setItem_total_price(new_item_total_price);
        item.setQuantity(new_quantity);
        // set order item quantity type
        item.setQuantity_type("pc"); // fixed it since it will always be pc.

        orderItemRepo.save(item);
        orderRepo.save(order);

        // Update product inventory
        Product product = productRepo.findProductById(item.getProduct_id());
        if (product != null) {
            Long original_quantity = product.getQuantity();
            Long updated_quantity = original_quantity - item.getQuantity();
            productRepo.updateProductInventory(product.getId(), updated_quantity);
        }
    }
}
