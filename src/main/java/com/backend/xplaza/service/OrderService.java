package com.backend.xplaza.service;

import com.backend.xplaza.model.Order;
import com.backend.xplaza.model.OrderDetails;
import com.backend.xplaza.model.OrderList;
import com.backend.xplaza.repository.OrderDetailsRepository;
import com.backend.xplaza.repository.OrderListRepository;
import com.backend.xplaza.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addOrder(Order order) {
        orderRepo.save(order);
    }

    public void updateOrder(Order order) {
        orderRepo.save(order);
    }

    public List<OrderList> listOrders() { return orderListRepo.findAllOrders(); }

    public OrderDetails listOrderDetails(long id) { return orderDetailsRepo.findOrderDetailsById(id); }

    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    public List<OrderList> listOrdersByFilter(String status, Date order_date) {
        return orderListRepo.findAllOrdersByFilter(status,order_date);
    }

    public List<OrderList> listOrdersByStatus(String status) {
        return orderListRepo.findAllOrdersByStatus(status);
    }

    public void updateOrderStatus(long invoice_number, long status) {
        orderRepo.updateOrderStatus(invoice_number,status);
    }
}
