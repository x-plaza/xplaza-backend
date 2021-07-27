package com.backend.xplaza.service;

import com.backend.xplaza.model.Order;
import com.backend.xplaza.model.OrderItem;
import com.backend.xplaza.repository.OrderItemRepository;
import com.backend.xplaza.repository.OrderRepository;
import com.backend.xplaza.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;

    @Transactional
    public void addOrderItem(OrderItem orderItem) {
        Order order = orderRepo.findOrderById(orderItem.getOrder_id());
        orderItem.setItem_total_price(orderItem.getUnit_price() * orderItem.getQuantity());

        Double item_total_price = orderItem.getItem_total_price();
        Double total_price = order.getTotal_price();
        Double total_discount = order.getDiscount_amount();
        total_price = total_price + item_total_price;
        Double grand_total = total_price - total_discount;

        order.setTotal_price(total_price);
        order.setGrand_total_price(grand_total);
        orderItemRepo.save(orderItem);
        orderRepo.save(order);
    }

    public List<OrderItem> listOrderItems() { return orderItemRepo.findAll(); }

    public OrderItem listOrderItem(Long id) { return orderItemRepo.findOrderItemById(id); }

    public String getOrderItemNameByID(Long id) {
        return orderItemRepo.getName(id);
    }

    @Transactional
    public void deleteOrderItem(Long id) {
        OrderItem item = orderItemRepo.findOrderItemById(id);
        Order order = orderRepo.findOrderById(item.getOrder_id());

        Double item_total_price = item.getItem_total_price();
        Double total_price = order.getTotal_price();
        Double total_discount = order.getDiscount_amount();
        total_price = total_price - item_total_price;
        Double grand_total = total_price - total_discount;

        order.setTotal_price(total_price);
        order.setGrand_total_price(grand_total);
        orderItemRepo.deleteById(id);
        orderRepo.save(order);
    }

    @Transactional
    public void updateOrderItem(Long order_item_id, Long new_quantity) {
        OrderItem item = orderItemRepo.findOrderItemById(order_item_id);
        Order order = orderRepo.findOrderById(item.getOrder_id());
        //Product product = productRepo.findProductById(item.getProduct_id());

        // Get the old values:
        //Double original_price = product.getSelling_price();
        Double unit_price = item.getUnit_price();
        Double old_item_total_price = item.getItem_total_price();
        //Long old_quantity = item.getQuantity();
        Double total_price = order.getTotal_price();
        Double total_discount = order.getDiscount_amount();

        // Calculate the new values:
        //Double per_unit_discount = original_price - unit_price;
        //Double total_discount_per_item = per_unit_discount * old_quantity;
        Double new_item_total_price = unit_price * new_quantity;
        total_price = total_price - old_item_total_price + new_item_total_price;
        Double grand_total = total_price - total_discount;

        // Set the new values and save them:
        item.setItem_total_price(new_item_total_price);
        item.setQuantity(new_quantity);
        order.setTotal_price(total_price);
        order.setGrand_total_price(grand_total);
        orderItemRepo.save(item);
        orderRepo.save(order);
    }
}
