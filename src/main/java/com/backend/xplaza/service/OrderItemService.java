package com.backend.xplaza.service;

import com.backend.xplaza.model.OrderItem;
import com.backend.xplaza.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepo;

    public void addOrderItem(OrderItem orderItem) {
        orderItemRepo.save(orderItem);
    }

    public void updateOrderItem(OrderItem orderItem) {
        orderItemRepo.save(orderItem);
    }

    public List<OrderItem> listOrderItems() { return orderItemRepo.findAll(); }

    public OrderItem listOrderItem(long id) { return orderItemRepo.findOrderItemById(id); }

    public String getOrderItemNameByID(long id) {
        return orderItemRepo.getName(id);
    }

    public void deleteOrderItem(long id) {
        orderItemRepo.deleteById(id);
    }
}
