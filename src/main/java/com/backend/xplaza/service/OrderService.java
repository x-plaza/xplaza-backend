package com.backend.xplaza.service;

import com.backend.xplaza.model.*;
import com.backend.xplaza.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private CustomerUserRepository customerUserRepo;
    @Autowired
    private AdminUserRepository adminUserRepo;
    @Autowired
    private CurrencyRepository currencyRepo;

    @Transactional
    public ProductInventory checkProductAvailability (OrderPlace order){
        ProductInventory productInventory = new ProductInventory();
        productInventory.setIs_available(true);
        for (OrderItem item : order.getOrderItemList()) {
            Product product = productRepo.findProductById(item.getProduct_id());
            if(product.getQuantity() < item.getQuantity()) {
                productInventory.setId(product.getId());
                productInventory.setName(product.getName());
                productInventory.setMax_available_quantity(product.getQuantity());
                productInventory.setIs_available(false);
                break;
            }
        }
        return productInventory;
    }

    @Transactional
    public OrderPlace setOrderPrices (OrderPlace order){
        Double total_price = 0.0;
        Double total_discount = 0.0;
        Double net_total = 0.0;
        for (OrderItem item : order.getOrderItemList()) {
            Product product = productRepo.findProductById(item.getProduct_id());
            Double original_price = product.getSelling_price();
            // check discount amount with validity and discount type
            Double discount_amount = 0.0;
            ProductDiscount productDiscount = productDiscountRepo.findByProductId(item.getProduct_id());
            if(productDiscount != null)
            {
                discount_amount = productDiscount.getDiscount_amount();
                Long discount_type = productDiscount.getDiscount_type_id();
                if(discount_type == 2) // Percentage
                {
                    discount_amount = original_price * (discount_amount/100);
                }
            }
            // set prices to the item
            Double unit_price = original_price - discount_amount; // here unit price is basically the discounted price
            item.setUnit_price(unit_price);
            item.setProduct_selling_price(original_price);
            item.setItem_total_price(unit_price * item.getQuantity());

            total_price += original_price * item.getQuantity();
            net_total += item.getItem_total_price();
        }
        total_discount = total_price - net_total;
        order.setTotal_price(total_price);
        order.setDiscount_amount(total_discount);
        order.setNet_total(net_total); // sum of item total for an order after discount

        // set delivery cost
        Double delivery_cost = 0.0;
        if(order.getDelivery_cost_id() != null){
            DeliveryCostList dc = deliveryCostListRepo.findDeliveryCostById(order.getDelivery_cost_id());
            delivery_cost = dc.getCost();
        }
        order.setDelivery_cost(delivery_cost);

        // set coupon amount
        Double coupon_amount = 0.0;
        if(order.getCoupon_id() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());
            if(couponDetails.getDiscount_type_name().equals("Fixed Amount")) coupon_amount= couponDetails.getAmount();
            else { // for percentage
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(couponDetails.getMax_amount() != null) {
                    if (coupon_amount > couponDetails.getMax_amount())
                        coupon_amount = couponDetails.getMax_amount();
                }
            }
        }
        else if(order.getCoupon_code() != null) {
            CouponDetails couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
            if(couponDetails.getDiscount_type_name().equals("Fixed Amount")) coupon_amount= couponDetails.getAmount();
            else { // for percentage
                coupon_amount = (order.getNet_total() *  couponDetails.getAmount())/100;
                if(couponDetails.getMax_amount() != null) {
                    if (coupon_amount > couponDetails.getMax_amount())
                        coupon_amount = couponDetails.getMax_amount();
                }
            }
        }
        order.setCoupon_amount(coupon_amount);
        order.setGrand_total_price(net_total + delivery_cost - coupon_amount);
        return order;
    }

    @Transactional
    public boolean checkCouponValidity(OrderPlace order, String type) throws ParseException {
        CouponDetails couponDetails = null;
        if(type.equals("Code")) couponDetails = couponDetailsRepo.findCouponDetailsByCode(order.getCoupon_code());
        else couponDetails = couponDetailsRepo.findCouponDetailsById(order.getCoupon_id());

        Date received_time = order.getReceived_time();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        if (!(received_time.compareTo(formatter.parse(couponDetails.getStart_date())) >= 0
                && received_time.compareTo(formatter.parse(couponDetails.getEnd_date())) <= 0))
            return false;
        if (!couponDetails.getIs_active())  return false;
        if (order.getNet_total() < couponDetails.getMin_shopping_amount()) return false;

        // check if coupon is available for this shop?
        boolean is_valid = false;
        for(CouponShopList shop : couponDetails.getShopList())
        {
            if (Objects.equals(shop.getShop_id(), order.getShop_id()))
            {
                is_valid = true;
                break;
            }
        }
        return is_valid;
    }

    @Transactional
    public OrderPlace addOrder(OrderPlace order) {
        order = orderPlaceRepo.save(order);
        for (OrderItem oi : order.getOrderItemList()) {
            oi.setOrder_id(order.getInvoice_number());
            orderItemRepo.save(oi);
            // update product inventory
            Product product = productRepo.findProductById(oi.getProduct_id());
            Long original_quantity = product.getQuantity();
            Long new_quantity = original_quantity - oi.getQuantity();
            productRepo.updateProductInventory(product.getId(), new_quantity);
        }
        return order;
    }

    @Transactional
    public void updateOrder(OrderPlace order) {
        order = orderPlaceRepo.save(order);
        for (OrderItem oi : order.getOrderItemList()) {
            orderItemRepo.save(oi);
        }
    }

    @Transactional
    public void updateOrderStatus(Long order_id, String remarks, Long status) {
        orderRepo.updateOrderStatus(order_id,remarks,status);
        if(status == 6) // If Order is Cancelled, revert Product Inventory
        {
            OrderPlace order = new OrderPlace();
            Optional<OrderPlace> orderPlace = orderPlaceRepo.findById(order_id);
            if (orderPlace.isPresent()) order = orderPlace.get();
            // update product inventory
            for (OrderItem oi : order.getOrderItemList()) {
                Product product = productRepo.findProductById(oi.getProduct_id());
                Long original_quantity = product.getQuantity();
                Long new_quantity = original_quantity + oi.getQuantity();
                productRepo.updateProductInventory(product.getId(), new_quantity);
            }
        }
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
    public OrderDetails listOrderDetails(Long id) { return orderDetailsRepo.findOrderDetails(id); }

    // For any customer user
    public List<OrderPlaceList> listOrdersByCustomer(Long customer_id) { return orderPlaceListRepo.findAllOrdersByCustomer(customer_id); }

    public List<OrderPlaceList> listOrdersByStatusByCustomer(Long customer_id, String status) {
        return orderPlaceListRepo.findAllOrdersByStatusByCustomer(customer_id,status);
    }

    public List<OrderPlaceList> listOrdersByFilterByCustomer(Long customer_id, String status, Date order_date) {
        return orderPlaceListRepo.findAllOrdersByFilterByCustomer(customer_id,status,order_date);
    }

    public void sendOrderDetailsToCustomer(OrderPlace order, OrderResponse dtos, PlatformInfo platformInfo) {
        // get currency
        String currency_name = currencyRepo.getName(order.getCurrency_id());

        // format date and time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String delivery_date = dateFormatter.format(order.getDate_to_deliver());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String delivery_schedule = timeFormatter.format(order.getDelivery_schedule_start()) + " - " +
                timeFormatter.format(order.getDelivery_schedule_end());

        // send email to customer
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String email = customerUserRepo.getUsername(order.getCustomer_id());
        mailMessage.setTo(email);
        mailMessage.setSubject("Your "+ platformInfo.getName()+".com Order.");
        mailMessage.setText("Dear "+ order.getCustomer_name() +",\n\n" +
                "Thank you for your order. We’ll let you know once your item(s) have dispatched.\n\n" +
                "The summary of your order is as follows:\n" +
                        "Order No : " + dtos.getInvoice_number() + "\n" +
                        "Grand Total : " + dtos.getGrand_total_price() + " "+ currency_name +"\n" +
                        "Delivery Date : " + delivery_date + "\n" +
                        "Delivery Schedule : " + delivery_schedule + "\n\n" +
                "You can view the details of your order by visiting My Orders on https://"+ platformInfo.getName()+".com.\n\n" +
                "With Regards,\n"+ "Team Xwinkel"
        );
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendOrderDetailsToShopAdmin(OrderPlace order, OrderResponse dtos, PlatformInfo platformInfo) {
        // get currency
        String currency_name = currencyRepo.getName(order.getCurrency_id());

        // format date and time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String delivery_date = dateFormatter.format(order.getDate_to_deliver());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String delivery_schedule = timeFormatter.format(order.getDelivery_schedule_start()) + " - " +
                timeFormatter.format(order.getDelivery_schedule_end());

        // send email to shop admins
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        List<String> emailList = adminUserRepo.getEmailList(order.getShop_id());
        for (String email: emailList) {
            if (email.equals("admin@gmail.com")) continue;
            else {
                mailMessage.setTo(email);
                mailMessage.setSubject(platformInfo.getName()+".com Customer Order.");
                mailMessage.setText("Hello,\n\n" +
                        "The following order has been placed by the customer: " + order.getCustomer_name() +".\n\n" +
                        "You can view the order details by visiting Pending Orders on https://admin."+ platformInfo.getName().toLowerCase() + ".com.\n\n" +
                        "Order No : " + dtos.getInvoice_number() + "\n" +
                        "Grand Total : " + dtos.getGrand_total_price() + " "+ currency_name +"\n" +
                        "Delivery Date : " + delivery_date + "\n" +
                        "Delivery Schedule : " + delivery_schedule
                );
                emailSenderService.sendEmail(mailMessage);
            }
        }
    }
}
